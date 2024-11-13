package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.ProjectDto;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.FULL_TIME;
import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.PART_TIME;
import static cz.cvut.fel.pm2.timely_be.mapper.MapperUtils.toEmployeeDto;
import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.createEmployee;
import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.createProject;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ProjectService projectService;

    private ProjectDto projectDto;

    @BeforeEach
    public void setUp() {
        projectDto = new ProjectDto();
        projectDto.setName("Test Project");
        projectDto.setManagerId(1L);
        projectDto.setMembers(Collections.emptyList());
    }

    @Test
    public void testCreateProject() {
        // Given
        var project = new Project();
        project.setName(projectDto.getName());

        when(projectRepository.save(any(Project.class))).thenReturn(project);
        // Mock the manager
        Employee manager = new Employee();
        manager.setEmployeeId(1L);
        manager.setName("Manager Name");

        // Configure employeeRepository to return the manager when findById is called with managerId
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));

        // When
        var result = projectService.createProject(projectDto);

        // Then
        assertNotNull(result);
        assertEquals(projectDto.getName(), result.getName());
    }

    @Test
    public void testGetAllProjects() {
        // Given
        var project1 = createProject();
        var project2 = createProject();
        List<Project> projects = Arrays.asList(project1, project2);

        when(projectRepository.findAll()).thenReturn(projects);

        // When
        var result = projectService.getAllProjects();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        // Only verify basic properties, not members
        assertEquals(project1.getProjectId(), result.get(0).getProjectId());
        assertEquals(project1.getName(), result.get(0).getName());
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

    @Test
    public void testGetProjectsByEmployeeId() {
        // Given
        var employee = new Employee();
        var project = createProject();
        employee.setCurrentProjects(Collections.singletonList(project));

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        // When
        var result = projectService.getProjectsByEmployeeId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(project, result.get(0));
    }

    @Test
    public void testUpdateProject() {
        // Given
        var project = createProject();
        projectDto.setName("Updated Project");

        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(employeeRepository.findById(projectDto.getManagerId())).thenReturn(Optional.of(new Employee()));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // When
        var updatedProject = projectService.updateProject(project.getProjectId(), projectDto);

        // Then
        assertNotNull(updatedProject);
        assertEquals(projectDto.getName(), updatedProject.getName());
    }

    @Test
    public void testDeleteProject() {
        // Given
        var project = createProject();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        // When & Then (just to ensure no exceptions)
        assertDoesNotThrow(() -> projectService.deleteProject(project.getProjectId()));
    }

    @Test
    public void testCreateProjectThrowsExceptionWhenManagerNotFound() {
        // Given
        projectDto.setManagerId(99L);
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> projectService.createProject(projectDto));
        assertEquals("Manager not found", exception.getMessage());
    }

    @Test
    public void testUpdateProjectThrowsExceptionWhenProjectNotFound() {
        // Given
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> projectService.updateProject(1L, projectDto));
        assertEquals("Project not found", exception.getMessage());
    }

    @Test
    public void testUpdateProjectThrowsExceptionWhenEmployeeNotFound() {
        // Given
        var project = createProject();
        var employee = createEmployee(FULL_TIME);
        employee.setEmployeeId(99L); // Set ID to match the one we're testing for

        projectDto.setMembers(Collections.singletonList(toEmployeeDto(employee)));  // Mock employee with ID 99

        // Mocking repository behavior
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee())); // Stubbing for manager with ID 1L
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty()); // Stubbing to simulate missing employee

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> projectService.updateProject(1L, projectDto));
        assertEquals("Employee with ID 99 not found", exception.getMessage());
    }

    @Test
    public void testGetProjectWithMembers() {
        // Given
        var project = createProject();
        when(projectRepository.findProjectWithMembers(anyLong())).thenReturn(Optional.of(project));

        // When
        var result = projectService.getProjectWithMembers(1L);

        // Then
        assertNotNull(result);
        assertEquals(project.getProjectId(), result.getProjectId());
        assertNotNull(result.getMembers());
    }

    @Test
    public void testGetProjectDetails() {
        // Given
        var project = createProject();
        var employee1 = createEmployee(FULL_TIME);
        var employee2 = createEmployee(PART_TIME);
        project.setMembers(Arrays.asList(employee1, employee2));
        
        when(projectRepository.findProjectWithMembers(anyLong())).thenReturn(Optional.of(project));

        // When
        var result = projectService.getProjectDetails(1L);

        // Then
        assertNotNull(result);
        assertEquals(project.getProjectId(), result.getProjectId());
        assertEquals(project.getName(), result.getName());
        assertNotNull(result.getMembers());
        assertEquals(2, result.getMembers().size());
    }

    @Test
    public void testGetProjectDetails_NotFound() {
        // Given
        when(projectRepository.findProjectWithMembers(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> projectService.getProjectDetails(1L));
    }

}
