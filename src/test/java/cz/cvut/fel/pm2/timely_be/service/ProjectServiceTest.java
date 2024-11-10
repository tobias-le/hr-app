package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.createProject;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    public void testCreateProject() {
        // Given
        var projectName = "New Project";
        var project = new Project();
        project.setName(projectName);

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // When
        var result = projectService.createProject(projectName);

        // Then
        assertNotNull(result);
        assertEquals(projectName, result.getName());
    }

    @Test
    public void testGetAllProjects() {
        // Given
        var project1 = createProject();
        var project2 = createProject();
        var projects = Arrays.asList(project1, project2);

        when(projectRepository.findAll()).thenReturn(projects);

        // When
        var result = projectService.getAllProjects();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(projects, result);
    }

    @Test
    public void testGetAllProjectsWhenEmpty() {
        // Given
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        var result = projectService.getAllProjects();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty project list");
    }
}
