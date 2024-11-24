package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.EmployeeDto;
import cz.cvut.fel.pm2.timely_be.dto.EmployeeNameWithIdDto;
import cz.cvut.fel.pm2.timely_be.dto.ProjectNameWithIdDto;
import cz.cvut.fel.pm2.timely_be.dto.TeamNameWithIdDto;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Team;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.ProjectRepository;
import cz.cvut.fel.pm2.timely_be.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.FULL_TIME;
import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.PART_TIME;
import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void testGetEmployees() {
        // Given
        var teamId = 1L;
        var employee1 = createEmployee(FULL_TIME);
        var employee2 = createEmployee(PART_TIME);
        var employees = Arrays.asList(employee1, employee2);

        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(employees, pageable, employees.size());

        when(employeeRepository.findByTeamId(eq(pageable), eq(teamId))).thenReturn(page);

        // When
        var result = employeeService.getEmployeesByTeam(pageable, teamId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(employees, result.getContent());
    }

    @Test
    public void testUpdateEmployee() {
        // Given
        var employee = createEmployee(FULL_TIME);
        var employeeDto = new EmployeeDto();
        var team = new Team();
        team.setId(1L);
        team.setName("Test Team");
        
        // Create test projects
        var project1 = createProject();
        project1.setProjectId(1L);
        project1.setName("Project 1");
        
        var project2 = createProject();
        project2.setProjectId(2L);
        project2.setName("Project 2");

        // Set up the DTO
        employeeDto.setName("John Doe");
        employeeDto.setEmail("john@example.com");
        employeeDto.setEmploymentStatus(PART_TIME.toString());
        employeeDto.setJobTitle("Developer");
        employeeDto.setPhoneNumber("123456789");
        employeeDto.setAnnualSalary(50000);
        employeeDto.setAnnualLearningBudget(1000);
        employeeDto.setAnnualBusinessPerformanceBonusMax(5000);
        employeeDto.setAnnualPersonalPerformanceBonusMax(2500);
        employeeDto.setTeam(new TeamNameWithIdDto(team.getId(), team.getName()));
        employeeDto.setCurrentProjects(Arrays.asList(
            new ProjectNameWithIdDto(project1.getProjectId(), project1.getName()),
            new ProjectNameWithIdDto(project2.getProjectId(), project2.getName())
        ));

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(projectRepository.findById(project1.getProjectId())).thenReturn(Optional.of(project1));
        when(projectRepository.findById(project2.getProjectId())).thenReturn(Optional.of(project2));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> i.getArgument(0));

        // When
        var result = employeeService.updateEmployee(1L, employeeDto);

        // Then
        assertNotNull(result);
        assertEquals(employeeDto.getName(), result.getName());
        assertEquals(employeeDto.getEmail(), result.getEmail());
        assertEquals(PART_TIME, result.getEmploymentType());
        assertEquals(employeeDto.getJobTitle(), result.getJobTitle());
        assertEquals(employeeDto.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(employeeDto.getAnnualSalary(), result.getAnnualSalary());
        assertEquals(employeeDto.getAnnualLearningBudget(), result.getAnnualLearningBudget());
        assertEquals(employeeDto.getAnnualBusinessPerformanceBonusMax(), result.getAnnualBusinessPerformanceBonusMax());
        assertEquals(employeeDto.getAnnualPersonalPerformanceBonusMax(), result.getAnnualPersonalPerformanceBonusMax());
        assertEquals(team, result.getTeam());
        assertEquals(2, result.getCurrentProjects().size());
        assertTrue(result.getCurrentProjects().contains(project1));
        assertTrue(result.getCurrentProjects().contains(project2));
    }

    @Test
    public void testUpdateEmployee_TeamNotFound() {
        // Given
        var employee = createEmployee(FULL_TIME);
        var employeeDto = new EmployeeDto();
        
        // Set up the DTO with a non-existent team
        employeeDto.setTeam(new TeamNameWithIdDto(999L, "Non-existent Team"));
        
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> employeeService.updateEmployee(1L, employeeDto));
    }

    @Test
    public void testUpdateEmployee_ProjectNotFound() {
        // Given
        var employee = createEmployee(FULL_TIME);
        var employeeDto = new EmployeeDto();
        
        // Set up the DTO with a non-existent project
        employeeDto.setCurrentProjects(List.of(
            new ProjectNameWithIdDto(999L, "Non-existent Project")
        ));
        
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> employeeService.updateEmployee(1L, employeeDto),
            "Project not found: 999");
    }

    @Test
    public void testUpdateEmployee_RemoveProjects() {
        // Given
        var employee = createEmployee(FULL_TIME);
        var oldProject = createProject();
        oldProject.setProjectId(1L);
        employee.setCurrentProjects(new ArrayList<>(List.of(oldProject)));
        oldProject.setMembers(new HashSet<>(List.of(employee)));
        
        var employeeDto = new EmployeeDto();
        employeeDto.setCurrentProjects(Collections.emptyList()); // Remove all projects
        
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> i.getArgument(0));

        // When
        var result = employeeService.updateEmployee(1L, employeeDto);

        // Then
        assertNotNull(result);
        assertTrue(result.getCurrentProjects().isEmpty());
        assertFalse(oldProject.getMembers().contains(employee));
    }

    @Test
    public void testGetAllEmployees() {
        // Given
        var employee1 = createEmployee(FULL_TIME);
        var employee2 = createEmployee(PART_TIME);
        var employees = List.of(employee1, employee2);

        when(employeeRepository.findAll()).thenReturn(employees);

        // When
        var result = employeeService.getAllEmployees();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(employees, result);
    }

    @Test
    public void testGetEmployee() {
        // Given
        var employee = createEmployee(FULL_TIME);

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        // When
        var result = employeeService.getEmployee(1L);

        // Then
        assertNotNull(result);
        assertEquals(employee.getEmployeeId(), result.getEmployeeId());
        assertEquals(employee.getName(), result.getName());
        assertEquals(employee.getEmail(), result.getEmail());
        assertEquals(employee.getEmploymentType(), result.getEmploymentType());
    }

    @Test
    public void testGetEmployee_NotFound() {
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> employeeService.getEmployee(1L));
    }

    @Test
    public void testGetEmployeeNamesWithIds() {
        // Given
        var employee1 = createEmployee(FULL_TIME);
        var employee2 = createEmployee(PART_TIME);
        var expectedDtos = Arrays.asList(
            new EmployeeNameWithIdDto(employee1.getEmployeeId(), employee1.getName()),
            new EmployeeNameWithIdDto(employee2.getEmployeeId(), employee2.getName())
        );

        when(employeeRepository.findAllEmployeeNamesWithIds()).thenReturn(expectedDtos);

        // When
        var result = employeeService.getAllEmployeeNamesWithIds();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedDtos, result);
    }

    @Test
    public void testAutocompleteEmployees() {
        // Given
        String searchPattern = "joh";
        List<Long> excludeIds = List.of(3L);
        var employee1 = new EmployeeNameWithIdDto(1L, "John Doe");
        var employee2 = new EmployeeNameWithIdDto(2L, "Johnny Smith");
        var expectedResults = Arrays.asList(employee1, employee2);

        when(employeeRepository.findEmployeesByNameContaining(eq(searchPattern), eq(excludeIds), any(Pageable.class)))
                .thenReturn(expectedResults);

        // When
        var result = employeeService.autocompleteEmployees(searchPattern, excludeIds);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedResults, result);
    }

    @Test
    public void testAutocompleteEmployees_EmptyQuery() {
        // When
        var result = employeeService.autocompleteEmployees("", List.of());

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAutocompleteEmployees_NullQuery() {
        // When
        var result = employeeService.autocompleteEmployees(null, List.of());

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetEmployeesByTeam() {
        // Given
        var teamId = 1L;
        var employee1 = createEmployee(FULL_TIME);
        var employee2 = createEmployee(PART_TIME);
        var employees = Arrays.asList(employee1, employee2);
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(employees, pageable, employees.size());

        when(employeeRepository.findByTeamId(pageable, teamId)).thenReturn(page);

        // When
        var result = employeeService.getEmployeesByTeam(pageable, teamId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(employees, result.getContent());
    }

    @Test
    public void testGetEmployeesByProject() {
        // Given
        var projectId = 1L;
        var employee1 = createEmployee(FULL_TIME);
        var employee2 = createEmployee(PART_TIME);
        var employees = Arrays.asList(employee1, employee2);
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(employees, pageable, employees.size());

        when(employeeRepository.findEmployeesByProjectIdPageable(projectId, pageable)).thenReturn(page);

        // When
        var result = employeeService.getEmployeesByProject(pageable, projectId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(employees, result.getContent());
    }

    @Test
    public void testGetAllEmployeesPage() {
        // Given
        var employee1 = createEmployee(FULL_TIME);
        var employee2 = createEmployee(PART_TIME);
        var employees = Arrays.asList(employee1, employee2);
        
        when(employeeRepository.count()).thenReturn((long) employees.size());
        var pageable = PageRequest.of(0, employees.size());
        var page = new PageImpl<>(employees, pageable, employees.size());
        
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        // When
        var result = employeeService.getAllEmployeesPage();

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(employees, result.getContent());
    }

    @Test
    public void testUpdateEmployee_NotFound() {
        // Given
        var employeeId = 999L;
        var employeeDto = createEmployeeDto(FULL_TIME);
        
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> employeeService.updateEmployee(employeeId, employeeDto));
    }

    @Test
    public void testAutocompleteEmployees_NullExcludeIds() {
        // Given
        String searchPattern = "john";
        var employee1 = new EmployeeNameWithIdDto(1L, "John Doe");
        var employee2 = new EmployeeNameWithIdDto(2L, "Johnny Smith");
        var expectedResults = Arrays.asList(employee1, employee2);

        when(employeeRepository.findEmployeesByNameContaining(eq(searchPattern), eq(Collections.emptyList()), any(Pageable.class)))
                .thenReturn(expectedResults);

        // When
        var result = employeeService.autocompleteEmployees(searchPattern, null);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedResults, result);
    }
}
