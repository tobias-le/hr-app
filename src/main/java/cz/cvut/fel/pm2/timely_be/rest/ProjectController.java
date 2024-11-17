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
    @Operation(summary = "Create a new project", description = "Creates a new project with the given data")
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        Project project = projectService.createProject(projectDto);
        return ResponseEntity.created(
                fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(project.getProjectId())
                        .toUri()
        ).body(toProjectDto(project));
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "Update a project", description = "Updates an existing project with new data")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long projectId, @RequestBody ProjectDto projectDto) {
        Project updatedProject = projectService.updateProject(projectId, projectDto);
        return ResponseEntity.ok(toProjectDto(updatedProject));
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "Delete a project", description = "Deletes a project by its ID")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Returns a list of all projects without member details")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Get project details", description = "Returns project details including members")
    public ResponseEntity<ProjectDto> getProject(@PathVariable Long projectId) {
        Project project = projectService.getProjectWithMembers(projectId);
        return ResponseEntity.ok(MapperUtils.toProjectDto(project));
    }

    @GetMapping("/by-employee/{employeeId}")
    @Operation(summary = "Get projects by employee ID", description = "Returns a list of projects for the given employee")
    public ResponseEntity<List<ProjectDto>> getProjectsByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(
                projectService
                        .getProjectsByEmployeeId(employeeId)
                        .stream()
                        .map(MapperUtils::toProjectDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{projectId}/details")
    @Operation(summary = "Get detailed project information", description = "Returns project details including all members")
    public ResponseEntity<ProjectDto> getProjectDetails(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectDetails(projectId));
    }
}
