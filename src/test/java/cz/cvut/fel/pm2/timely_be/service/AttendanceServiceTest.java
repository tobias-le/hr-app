package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.AttendanceRecordDto;
import cz.cvut.fel.pm2.timely_be.model.AttendanceRecord;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.repository.AttendanceRecordRepository;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.FULL_TIME;
import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.createAttendanceRecord;
import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.createEmployee;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @Mock
    private AttendanceRecordRepository attendanceRecordRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @BeforeEach
    public void setUp() {
        // Setup code if necessary
    }

    @Test
    public void testGetAttendanceRecordsByMember() {
        // Given
        var member = createEmployee(FULL_TIME);
        var record1 = createAttendanceRecord(member, member.getCurrentProjects().get(0));
        var record2 = createAttendanceRecord(member, member.getCurrentProjects().get(0));
        var records = List.of(record1, record2);

        when(attendanceRecordRepository.findByMember(member)).thenReturn(records);

        // When
        var result = attendanceService.getAttendanceRecordsByMember(member);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAttendanceRecordById() {
        // Given
        var attendanceId = 1L;
        var employee = createEmployee(FULL_TIME);
        var record = createAttendanceRecord(employee, employee.getCurrentProjects().get(0));
        record.setAttendanceId(attendanceId);


        when(attendanceRecordRepository.findById(attendanceId)).thenReturn(Optional.of(record));

        // When
        var result = attendanceService.getAttendanceRecordById(attendanceId);

        // Then
        assertTrue(result.isPresent());
    }

    @Test
    public void testGetCurrentWeekAttendancePerformanceForProject() {
        // Given
        var projectId = 1L;
        var project = new Project();
        project.setProjectId(projectId);
        project.setName("Project A");

        var employee1 = createEmployee(FULL_TIME);
        var employee2 = createEmployee(FULL_TIME);
        project.setMembers(Set.of(employee1, employee2));

        var startDate = now().with(MONDAY);
        var endDate = now().with(SUNDAY);

        var record1 = createAttendanceRecord(employee1, project, startDate, 9, 17); // 8 hours
        var record2 = createAttendanceRecord(employee2, project, startDate.plusDays(1), 10, 14); // 4 hours
        var records = List.of(record1, record2);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(attendanceRecordRepository.findByProjectIdAndDateBetween(projectId, startDate, endDate))
                .thenReturn(records);
        when(employeeRepository.findEmployeesByProjectId(projectId)).thenReturn(List.of(employee1, employee2));

        // When
        var result = attendanceService.getAttendanceSummaryForProject(projectId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals("Project A", result.getProjectName());
        assertEquals(12, result.getTotalHours()); // 8 + 4 hours
        var expectedHoursPerWeek = employee1.getEmploymentType().getExpectedHoursPerDay() * 5
                + employee2.getEmploymentType().getExpectedHoursPerDay() * 5;
        assertEquals(expectedHoursPerWeek, result.getExpectedHours());
        double averageHoursPerDay = (double) result.getTotalHours() / 5;
        assertEquals(averageHoursPerDay, result.getAverageHoursPerDay(), 0.01);
        int totalDaysInRange = (int) (Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays()) + 1;
        double attendanceRate = (double) records.size() / (project.getMembers().size() * totalDaysInRange) * 100;
        assertEquals(attendanceRate, result.getAttendanceRate(), 0.01);
    }

    @Test
    public void testCreateAttendanceRecord() {
        // Given
        var attendanceRecordDto = new AttendanceRecordDto();
        var employee = createEmployee(FULL_TIME);
        var project = employee.getCurrentProjects().get(0);

        attendanceRecordDto.setMemberId(employee.getEmployeeId());
        attendanceRecordDto.setProject(project.getName());
        attendanceRecordDto.setDate(LocalDate.now());
        attendanceRecordDto.setClockInTime(LocalDateTime.now().withHour(9));
        attendanceRecordDto.setClockOutTime(LocalDateTime.now().withHour(17));

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(attendanceRecordRepository.save(any(AttendanceRecord.class))).thenReturn(new AttendanceRecord());

        // When
        var result = attendanceService.createAttendanceRecord(attendanceRecordDto);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testUpdateAttendanceRecordById() {
        // Given
        var attendanceId = 1L;
        var attendanceRecordDto = new AttendanceRecordDto();
        var employee = createEmployee(FULL_TIME);
        var project = employee.getCurrentProjects().get(0);

        attendanceRecordDto.setMemberId(employee.getEmployeeId());
        attendanceRecordDto.setProject(project.getName());
        attendanceRecordDto.setDate(LocalDate.now());
        attendanceRecordDto.setClockInTime(LocalDateTime.now().withHour(9));
        attendanceRecordDto.setClockOutTime(LocalDateTime.now().withHour(17));

        var existingRecord = new AttendanceRecord();
        existingRecord.setAttendanceId(attendanceId);

        when(attendanceRecordRepository.findById(attendanceId)).thenReturn(Optional.of(existingRecord));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(attendanceRecordRepository.save(any(AttendanceRecord.class))).thenReturn(new AttendanceRecord());

        // When
        var result = attendanceService.updateAttendanceRecordById(attendanceId, attendanceRecordDto);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testDeleteAttendanceRecordById() {
        // Given
        var attendanceId = 1L;

        // When & Then
        assertDoesNotThrow(() -> attendanceService.deleteAttendanceRecordById(attendanceId));
    }

    @Test
    public void testGetAttendanceRecordsByProjectSinceStartOfWeek() {
        // Given
        var projectId = 1L;
        var employee = createEmployee(FULL_TIME);
        var project = employee.getCurrentProjects().get(0);
        var startDate = now().with(MONDAY);
        var endDate = now(); // Assuming you want to test up to today

        var record1 = createAttendanceRecord(employee, project, startDate, 9, 17);
        var record2 = createAttendanceRecord(employee, project, endDate, 8, 16);
        var records = List.of(record1, record2);

        when(attendanceRecordRepository.findByProjectIdAndDateBetween(projectId, startDate, endDate))
                .thenReturn(records);

        // When
        var result = attendanceService.getAttendanceRecordsByProject(projectId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetCurrentWeekAttendancePerformanceForProject_ProjectNotFound() {
        // Given
        var projectId = 999L;
        var startDate = now().with(MONDAY);
        var endDate = now().with(SUNDAY);
        
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // When
        var result = attendanceService.getAttendanceSummaryForProject(projectId, startDate, endDate);

        // Then
        assertNull(result);
    }

    @Test
    public void testUpdateAttendanceRecordById_RecordNotFound() {
        // Given
        var attendanceId = 999L;
        var attendanceRecordDto = new AttendanceRecordDto();
        
        when(attendanceRecordRepository.findById(attendanceId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, 
            () -> attendanceService.updateAttendanceRecordById(attendanceId, attendanceRecordDto));
    }

    @Test
    public void testCreateAttendanceRecord_EmployeeNotFound() {
        // Given
        var attendanceRecordDto = new AttendanceRecordDto();
        attendanceRecordDto.setMemberId(999L);

        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, 
            () -> attendanceService.createAttendanceRecord(attendanceRecordDto));
    }

    @Test
    public void testCreateAttendanceRecord_ProjectNotFound() {
        // Given
        var attendanceRecordDto = new AttendanceRecordDto();
        var employee = createEmployee(FULL_TIME);
        attendanceRecordDto.setMemberId(employee.getEmployeeId());
        attendanceRecordDto.setProject("Non-existent Project");

        when(employeeRepository.findById(employee.getEmployeeId())).thenReturn(Optional.of(employee));

        // When & Then
        assertThrows(NoSuchElementException.class,
                () -> attendanceService.createAttendanceRecord(attendanceRecordDto));
    }

    @Test
    public void testCreateAttendanceRecord_BoundaryHours() {
        // Given
        var attendanceRecordDto = new AttendanceRecordDto();
        var employee = createEmployee(FULL_TIME);
        var project = employee.getCurrentProjects().get(0);

        attendanceRecordDto.setMemberId(employee.getEmployeeId());
        attendanceRecordDto.setProject(project.getName());
        attendanceRecordDto.setDate(LocalDate.now());
        attendanceRecordDto.setClockInTime(LocalDateTime.now().withHour(0).withMinute(0)); // 00:00
        attendanceRecordDto.setClockOutTime(LocalDateTime.now().withHour(23).withMinute(59)); // 23:59

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(attendanceRecordRepository.save(any(AttendanceRecord.class))).thenReturn(new AttendanceRecord());

        // When
        var result = attendanceService.createAttendanceRecord(attendanceRecordDto);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testGetAttendanceRecordsByProjectSinceStartOfWeek_NoRecords() {
        // Given
        var projectId = 1L;
        var startDate = now().with(MONDAY);
        var endDate = now().with(SUNDAY);
        
        when(attendanceRecordRepository.findByProjectIdAndDateBetween(
                eq(projectId), 
                eq(startDate), 
                eq(endDate)))
                .thenReturn(List.of());

        // When
        var result = attendanceService.getAttendanceRecordsByProject(projectId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCurrentWeekAttendancePerformanceForProject_NoEmployees() {
        // Given
        var projectId = 1L;
        var startDate = now().with(MONDAY);
        var endDate = now().with(SUNDAY);
        
        var project = new Project();
        project.setProjectId(projectId);
        project.setName("Empty Project");
        project.setMembers(Set.of());

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(attendanceRecordRepository.findByProjectIdAndDateBetween(
                eq(projectId), 
                eq(startDate), 
                eq(endDate)))
                .thenReturn(List.of());
        when(employeeRepository.findEmployeesByProjectId(projectId)).thenReturn(List.of());

        // When
        var result = attendanceService.getAttendanceSummaryForProject(projectId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals("Empty Project", result.getProjectName());
        assertEquals(0, result.getTotalHours());
        assertEquals(0, result.getExpectedHours());
        assertEquals(0.0, result.getAttendanceRate(), 0.01);
        assertEquals(0.0, result.getAverageHoursPerDay(), 0.01);
    }

    @Test
    public void testGetAttendanceRecordsByProject() {
        // Given
        var projectId = 1L;
        var startDate = now().with(MONDAY);
        var endDate = now().with(SUNDAY);
        
        var employee = createEmployee(FULL_TIME);
        var project = employee.getCurrentProjects().get(0);
        project.setProjectId(projectId);
        
        // Create multiple records across different days
        var record1 = createAttendanceRecord(employee, project, startDate, 9, 17); // 8 hours on Monday
        var record2 = createAttendanceRecord(employee, project, startDate.plusDays(2), 10, 18); // 8 hours on Wednesday
        var record3 = createAttendanceRecord(employee, project, endDate, 9, 13); // 4 hours on Sunday
        var records = List.of(record1, record2, record3);

        when(attendanceRecordRepository.findByProjectIdAndDateBetween(
                eq(projectId), 
                eq(startDate), 
                eq(endDate)))
                .thenReturn(records);

        // When
        var result = attendanceService.getAttendanceRecordsByProject(projectId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        
        // Verify the contents of the returned DTOs
        var firstRecord = result.get(0);
        assertEquals(employee.getEmployeeId(), firstRecord.getMemberId());
        assertEquals(project.getName(), firstRecord.getProject());
        assertEquals(startDate, firstRecord.getDate());
        assertEquals(9, firstRecord.getClockInTime().getHour());
        assertEquals(17, firstRecord.getClockOutTime().getHour());
    }

    @Test
    public void testGetAttendanceSummaryForProject() {
        // Given
        var projectId = 1L;
        var startDate = now().with(MONDAY);
        var endDate = now().with(SUNDAY);
        
        // Create project with two employees
        var project = new Project();
        project.setProjectId(projectId);
        project.setName("Test Project");
        
        var employee1 = createEmployee(FULL_TIME);
        var employee2 = createEmployee(FULL_TIME);
        project.setMembers(Set.of(employee1, employee2));
        
        // Create attendance records for both employees
        var records = new ArrayList<AttendanceRecord>();
        
        // Employee 1: Works 8 hours on Monday and Tuesday (16 hours total)
        records.add(createAttendanceRecord(employee1, project, startDate, 9, 17));
        records.add(createAttendanceRecord(employee1, project, startDate.plusDays(1), 9, 17));
        
        // Employee 2: Works 6 hours on Wednesday and Thursday (12 hours total)
        records.add(createAttendanceRecord(employee2, project, startDate.plusDays(2), 10, 16));
        records.add(createAttendanceRecord(employee2, project, startDate.plusDays(3), 10, 16));

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(attendanceRecordRepository.findByProjectIdAndDateBetween(
                eq(projectId), 
                eq(startDate), 
                eq(endDate)))
                .thenReturn(records);
        when(employeeRepository.findEmployeesByProjectId(projectId))
                .thenReturn(List.of(employee1, employee2));

        // When
        var result = attendanceService.getAttendanceSummaryForProject(projectId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals("Test Project", result.getProjectName());
        
        // Total hours should be 28 (16 + 12)
        assertEquals(28, result.getTotalHours());
        
        // Expected hours for 2 full-time employees for a week (8 hours * 5 days * 2 employees)
        var expectedHoursPerWeek = 2 * (employee1.getEmploymentType().getExpectedHoursPerDay() * 5);
        assertEquals(expectedHoursPerWeek, result.getExpectedHours());
        
        // Average hours per day should be 5.6 (28 hours / 5 working days)
        assertEquals(5.6, result.getAverageHoursPerDay(), 0.01);
        
        // Attendance rate: 4 days attended out of 14 possible days (2 employees * 7 days)
        int totalDaysInRange = 7; // Monday to Sunday
        double attendanceRate = (double) records.size() / (project.getMembers().size() * totalDaysInRange) * 100;
        assertEquals(attendanceRate, result.getAttendanceRate(), 0.01);
    }
}
