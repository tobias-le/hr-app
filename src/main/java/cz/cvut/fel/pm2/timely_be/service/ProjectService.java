package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    public Project createProject(String projectName) {
        Project project = new Project();
        project.setName(projectName);
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Iterable<Project> getProjectsByEmployeeId(Long employeeId) {
        var employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        return employee.getCurrentProjects();
    }
}
