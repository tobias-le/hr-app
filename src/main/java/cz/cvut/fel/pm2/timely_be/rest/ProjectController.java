package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.ProjectDto;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static cz.cvut.fel.pm2.timely_be.mapper.MapperUtils.toProjectDto;
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
    public ResponseEntity<ProjectDto> createProject(String projectName) {
        Project project = projectService.createProject(projectName);
        return ResponseEntity.created(
                fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(project.getProjectId())
                        .toUri()
        ).body(toProjectDto(project));
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Returns a list of all projects")
    public ResponseEntity<Iterable<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(
                projectService
                        .getAllProjects()
                        .stream()
                        .map(MapperUtils::toProjectDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{employeeId}")
    @Operation(summary = "Get projects by employee ID", description = "Returns a list of projects for the given employee")
    public ResponseEntity<List<ProjectDto>> getProjectsByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(
                projectService
                        .getProjectsByEmployeeId(employeeId)
                        .stream()
                        .map(MapperUtils::toProjectDto)
                        .toList()
        );
    }
}
