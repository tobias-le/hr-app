package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.EmployeeDto;
import cz.cvut.fel.pm2.timely_be.dto.EmployeeNameWithIdDto;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.model.Team;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.ProjectRepository;
import cz.cvut.fel.pm2.timely_be.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;
import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.fromString;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, TeamRepository teamRepository, ProjectRepository projectRepository) {
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
        this.projectRepository = projectRepository;
    }

    public Page<Employee> getEmployeesByTeam(Pageable pageable, long teamId) {
        return employeeRepository.findByTeamId(pageable, teamId);
    }

    public Page<Employee> getEmployeesByProject(Pageable pageable, long projectId) {
        return employeeRepository.findEmployeesByProjectIdPageable(projectId, pageable);
    }

    /**
     * this method only exists for the sake of testing, will be deleted later
     * @return list of all employees
     */
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Page<Employee> getAllEmployeesPage() {
        var pageable = PageRequest.of(0, (int) employeeRepository.count());
        return employeeRepository.findAll(pageable);
    }

    @Transactional
    public Employee updateEmployee(Long id, EmployeeDto employeeDto) {
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        // Update basic fields
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setEmploymentType(fromString(employeeDto.getEmploymentStatus()));
        employee.setJobTitle(employeeDto.getJobTitle());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());

        // Update financial fields
        employee.setAnnualSalary(employeeDto.getAnnualSalary());
        employee.setAnnualLearningBudget(employeeDto.getAnnualLearningBudget());
        employee.setAnnualBusinessPerformanceBonusMax(employeeDto.getAnnualBusinessPerformanceBonusMax());
        employee.setAnnualPersonalPerformanceBonusMax(employeeDto.getAnnualPersonalPerformanceBonusMax());

        // Update team if provided
        if (employeeDto.getTeam() != null) {
            Team team = teamRepository.findById(employeeDto.getTeam().getTeamId())
                    .orElseThrow(() -> new IllegalArgumentException("Team not found"));
            employee.setTeam(team);
        }

        // Update projects if provided
        if (employeeDto.getCurrentProjects() != null) {
            List<Project> newProjects = employeeDto.getCurrentProjects().stream()
                    .map(projectDto -> projectRepository.findById(projectDto.getProjectId())
                            .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectDto.getProjectId())))
                    .collect(Collectors.toList());
            
            // Remove employee from old projects that are not in the new list
            if (employee.getCurrentProjects() != null) {
                employee.getCurrentProjects().stream()
                        .filter(oldProject -> !newProjects.contains(oldProject))
                        .forEach(removedProject -> removedProject.getMembers().remove(employee));
            }
            
            employee.setCurrentProjects(newProjects);
        }

        return employeeRepository.save(employee);
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    public List<EmployeeNameWithIdDto> getAllEmployeeNamesWithIds() {
        return employeeRepository.findAllEmployeeNamesWithIds();
    }

    public List<EmployeeNameWithIdDto> autocompleteEmployees(String namePattern, List<Long> excludeIds) {
        if (namePattern == null || namePattern.trim().isEmpty()) {
            return Collections.emptyList();
        }
        if (excludeIds == null) {
            excludeIds = Collections.emptyList();
        }
        return employeeRepository.findEmployeesByNameContaining(namePattern.trim(), excludeIds, PageRequest.of(0, 5));
    }
}