package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Project", description = "The Project API")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project with the given name")
    public ResponseEntity<Project> createProject(String projectName) {
        Project project = projectService.createProject(projectName);
        return ResponseEntity.created(
                fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(project.getProjectId())
                        .toUri()
        ).body(project);
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Returns a list of all projects")
    public ResponseEntity<Iterable<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{employeeId}")
    @Operation(summary = "Get projects by employee ID", description = "Returns a list of projects for the given employee")
    public ResponseEntity<List<Project>> getProjectsByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(projectService.getProjectsByEmployeeId(employeeId));
    }
}
