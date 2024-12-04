package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.EmployeeNameWithIdDto;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.FULL_TIME;
import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.PART_TIME;
import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.createEmployee;
import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.createEmployeeDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserService userService;

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
        var employeeDto = createEmployeeDto(PART_TIME);

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any())).thenReturn(employee);

        // When
        var result = employeeService.updateEmployee(1L, employeeDto);

        // Then
        assertNotNull(result);
        assertEquals(employeeDto.getName(), result.getName());
        assertEquals(employeeDto.getEmail(), result.getEmail());
        assertEquals(PART_TIME, result.getEmploymentType());
        assertEquals(employeeDto.getJobTitle(), result.getJobTitle());
        assertEquals(employeeDto.getPhoneNumber(), result.getPhoneNumber());
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

    @Test
    public void testGetEmployeeByEmail() {
        // Given
        var email = "john.doe@example.com";
        var employee = createEmployee(FULL_TIME);
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(employee));

        // When
        var result = employeeService.getEmployeeByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(employee.getEmployeeId(), result.getEmployeeId());
        assertEquals(employee.getEmail(), result.getEmail());
    }

    @Test
    public void testGetEmployeeByEmail_NotFound() {
        // Given
        var email = "nonexistent@example.com";
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> employeeService.getEmployeeByEmail(email));
    }

    @Test
    public void testCreateEmployee_CreatesUserAndEmployee() {
        // Given
        var employeeDto = createEmployeeDto(FULL_TIME);
        when(employeeRepository.findByEmailIncludingDeleted(employeeDto.getEmail()))
                .thenReturn(Optional.empty());
        when(employeeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        
        // When
        var result = employeeService.createEmployee(employeeDto);

        // Then
        assertNotNull(result);
        assertEquals(employeeDto.getName(), result.getName());
        assertEquals(employeeDto.getEmail(), result.getEmail());
        assertEquals(FULL_TIME, result.getEmploymentType());
        assertFalse(result.isDeleted());
        
        verify(userService).createUsers(argThat(users -> 
            users.size() == 1 && 
            users.get(0).getEmail().equals(employeeDto.getEmail()) &&
            users.get(0).getEmployee() == result
        ));
    }

    @Test
    public void testDeleteEmployee() {
        // Given
        var employee = createEmployee(FULL_TIME);
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // When
        employeeService.deleteEmployee(1L);

        // Then
        assertTrue(employee.isDeleted());
        verify(employeeRepository).save(employee);
    }

    @Test
    public void testDeleteEmployee_NotFound() {
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> employeeService.deleteEmployee(1L));
    }
}
