package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.ProjectDto;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cz.cvut.fel.pm2.timely_be.mapper.MapperUtils.toProjectDto;

@Service
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Project createProject(ProjectDto projectDto) {
        // Check if active project with same name exists
        if (projectRepository.findByNameAndDeletedFalse(projectDto.getName()).isPresent()) {
            throw new IllegalArgumentException("Project with name '" + projectDto.getName() + "' already exists");
        }

        // Check if deleted project with same name exists and reuse it
        Optional<Project> deletedProject = projectRepository.findByNameAndDeletedTrue(projectDto.getName());
        Project project = deletedProject.orElse(new Project());
        
        toProject(projectDto, project);
        project.setDeleted(false);
        return projectRepository.save(project);
    }

    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(project -> {
                    ProjectDto dto = new ProjectDto();
                    dto.setProjectId(project.getProjectId());
                    dto.setName(project.getName());
                    dto.setManagerName(project.getManager().getName());
                    dto.setManagerId(project.getManager().getEmployeeId());
                    // Don't load members here
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Project getProjectWithMembers(Long projectId) {
        return projectRepository.findProjectWithMembers(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    public List<Project> getProjectsByEmployeeId(Long employeeId) {
        var employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        return employee.getCurrentProjects();
    }

    @Transactional
    public Project updateProject(Long projectId, ProjectDto projectDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        
        // Check if another active project with same name exists
        Optional<Project> existingProject = projectRepository.findByNameAndDeletedFalse(projectDto.getName());
        if (existingProject.isPresent() && !existingProject.get().getProjectId().equals(projectId)) {
            throw new IllegalArgumentException("Project with name '" + projectDto.getName() + "' already exists");
        }
        
        toProject(projectDto, project);
        return projectRepository.save(project);
    }

    private void toProject(ProjectDto projectDto, Project project) {
        project.setName(projectDto.getName());
        
        // Get manager
        Employee manager = employeeRepository.findById(projectDto.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));
        project.setManager(manager);
        
        // Get new members list and add manager if not present
        var newMembers = projectDto.getMembers()
                .stream()
                .map(employeeDto -> employeeRepository.findById(employeeDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + employeeDto.getId() + " not found")))
                .collect(Collectors.toSet());
        
        if (!newMembers.contains(manager)) {
            newMembers.add(manager);
        }
        
        // Remove project from old members that are not in new members list
        if (project.getMembers() != null) {
            project.getMembers().stream()
                    .filter(oldMember -> !newMembers.contains(oldMember))
                    .forEach(removedMember -> removedMember.getCurrentProjects().remove(project));
        }
        
        // Add project to all new members
        newMembers.forEach(member -> {
            if (!member.getCurrentProjects().contains(project)) {
                member.getCurrentProjects().add(project);
                employeeRepository.save(member);  // Save the updated employee
            }
        });
        
        project.setMembers(newMembers);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        log.info("Attempting to delete project with ID: {}", projectId);
        
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.error("Project with ID {} not found", projectId);
                    return new IllegalArgumentException("Project not found");
                });
        
        log.debug("Found project: {} (ID: {})", project.getName(), projectId);
        
        // Remove project from all members' currentProjects
        if (project.getMembers() != null) {
            log.info("Removing project from {} members", project.getMembers().size());
            project.getMembers().forEach(member -> {
                log.debug("Removing project from member: {} (ID: {})", member.getName(), member.getEmployeeId());
                member.getCurrentProjects().remove(project);
                employeeRepository.save(member);
            });
        }
        
        project.setDeleted(true);
        projectRepository.save(project);
        log.info("Successfully soft-deleted project with ID: {}", projectId);
    }

    public ProjectDto getProjectDetails(Long projectId) {
        Project project = projectRepository.findProjectWithMembers(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        return toProjectDto(project);
    }
}
