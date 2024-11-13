package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.FULL_TIME;
import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.PART_TIME;
import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.createEmployee;
import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.createEmployeeDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

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
}
