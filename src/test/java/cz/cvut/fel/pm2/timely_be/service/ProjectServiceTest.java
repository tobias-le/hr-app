package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.ProjectDto;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
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

import java.util.*;

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

        // Create a properly initialized employee for the members list
        var employee = createEmployee(FULL_TIME);
        employee.setCurrentProjects(new ArrayList<>());
        // Make sure the employee ID is consistent
        employee.setEmployeeId(2L); // Set a specific ID
        projectDto.setMembers(Set.of(toEmployeeDto(employee)));
    }

    @Test
    public void testGetAllProjects() {
        // Given
        var project1 = createProject();
        var project2 = createProject();
        List<Project> projects = Arrays.asList(project1, project2);

        when(projectRepository.findAll()).thenReturn(projects);

        var employee = createEmployee(FULL_TIME);
        employee.setEmployeeId(99L);

        projects
                .forEach(project -> {
                    project.setMembers(Set.of(employee));
                    project.setManager(employee);
                });

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

        projectDto.setMembers(Collections.singleton(toEmployeeDto(employee)));  // Mock employee with ID 99

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
        project.setMembers(Set.of(createEmployee(FULL_TIME)));
        when(projectRepository.findProjectWithMembers(anyLong())).thenReturn(Optional.of(project));

        // When
        var result = projectService.getProjectWithMembers(1L);

        // Then
        assertNotNull(result);
        assertEquals(project.getProjectId(), result.getProjectId());
        assertNotNull(result.getMembers());
    }

    @Test
    public void testGetProjectDetails_NotFound() {
        // Given
        when(projectRepository.findProjectWithMembers(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> projectService.getProjectDetails(1L));
    }

    @Test
    public void testCreateProject_Success() {
        // Given
        var manager = createEmployee(FULL_TIME);
        var member = createEmployee(FULL_TIME);

        projectDto.setManagerId(manager.getEmployeeId());
        projectDto.setMembers(Set.of(toEmployeeDto(member)));

        when(employeeRepository.findById(manager.getEmployeeId())).thenReturn(Optional.of(manager));
        when(employeeRepository.findById(member.getEmployeeId())).thenReturn(Optional.of(member));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        // When
        var result = projectService.createProject(projectDto);

        // Then
        assertNotNull(result);
        assertEquals(projectDto.getName(), result.getName());
        assertEquals(manager, result.getManager());
        assertTrue(result.getMembers().contains(manager), "Manager should be automatically added as a member");
        assertTrue(result.getMembers().contains(member));
    }

    @Test
    public void testUpdateProject_Success() {
        // Given
        var existingProject = createProject();
        var manager = createEmployee(FULL_TIME);
        var member = createEmployee(FULL_TIME);
        
        projectDto.setManagerId(manager.getEmployeeId());
        projectDto.setMembers(Set.of(toEmployeeDto(member)));

        when(projectRepository.findById(existingProject.getProjectId())).thenReturn(Optional.of(existingProject));
        when(employeeRepository.findById(manager.getEmployeeId())).thenReturn(Optional.of(manager));
        when(employeeRepository.findById(member.getEmployeeId())).thenReturn(Optional.of(member));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        // When
        var result = projectService.updateProject(existingProject.getProjectId(), projectDto);

        // Then
        assertNotNull(result);
        assertEquals(projectDto.getName(), result.getName());
        assertEquals(manager, result.getManager());
        assertTrue(result.getMembers().contains(manager), "Manager should be automatically added as a member");
        assertTrue(result.getMembers().contains(member));
    }

    @Test
    public void testGetProjectDetails_Success() {
        // Given
        var project = createProject();
        var manager = createEmployee(FULL_TIME);
        var member = createEmployee(FULL_TIME);
        
        project.setManager(manager);
        project.setMembers(Set.of(manager, member));

        when(projectRepository.findProjectWithMembers(project.getProjectId())).thenReturn(Optional.of(project));

        // When
        var result = projectService.getProjectDetails(project.getProjectId());

        // Then
        assertNotNull(result);
        assertEquals(project.getName(), result.getName());
        assertEquals(project.getManager().getEmployeeId(), result.getManagerId());
        assertEquals(2, result.getMembers().size());
    }

    @Test
    public void testUpdateProject_MembershipChanges() {
        // Given
        var existingProject = createProject();
        var oldMember = createEmployee(FULL_TIME);
        var newMember = createEmployee(PART_TIME);
        var manager = createEmployee(FULL_TIME);
        
        existingProject.setMembers(Set.of(oldMember, manager));
        existingProject.setManager(manager);
        oldMember.getCurrentProjects().add(existingProject);
        manager.getCurrentProjects().add(existingProject);
        
        var projectDto = new ProjectDto();
        projectDto.setName("Updated Project");
        projectDto.setManagerId(manager.getEmployeeId());
        projectDto.setMembers(Set.of(MapperUtils.toEmployeeDto(newMember)));

        when(projectRepository.findById(existingProject.getProjectId())).thenReturn(Optional.of(existingProject));
        when(employeeRepository.findById(manager.getEmployeeId())).thenReturn(Optional.of(manager));
        when(employeeRepository.findById(newMember.getEmployeeId())).thenReturn(Optional.of(newMember));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        // When
        var result = projectService.updateProject(existingProject.getProjectId(), projectDto);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getMembers().size()); // newMember + manager
        assertTrue(result.getMembers().contains(manager));
        assertTrue(result.getMembers().contains(newMember));
        assertFalse(result.getMembers().contains(oldMember));
        assertFalse(oldMember.getCurrentProjects().contains(result));
        assertTrue(newMember.getCurrentProjects().contains(result));
    }

    @Test
    public void testAutocompleteProjects() {
        // Given
        String searchPattern = "test";
        var project1 = new ProjectNameWithIdDto(1L, "Test Project");
        var project2 = new ProjectNameWithIdDto(2L, "Testing App");
        var expectedResults = Arrays.asList(project1, project2);

        when(projectRepository.findProjectsByNameContaining(
            eq(searchPattern), 
            eq(Collections.emptyList()), 
            any(Pageable.class)
        )).thenReturn(expectedResults);

        // When
        var result = projectService.autocompleteProjects(searchPattern, null);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedResults, result);
    }

}
