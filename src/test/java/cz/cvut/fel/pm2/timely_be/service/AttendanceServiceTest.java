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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        project.setMembers(List.of(employee1, employee2));

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        var startOfWeek = now().with(MONDAY);
        var endOfWeek = now().with(SUNDAY);

        var record1 = createAttendanceRecord(employee1, project, startOfWeek, 9, 17); // 8 hours
        var record2 = createAttendanceRecord(employee2, project, startOfWeek.plusDays(1), 10, 14); // 4 hours
        var records = List.of(record1, record2);

        when(attendanceRecordRepository.findByProjectIdAndDateBetween(eq(projectId), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(records);

        when(employeeRepository.findEmployeesByProjectId(projectId)).thenReturn(List.of(employee1, employee2));

        // When
        var result = attendanceService.getCurrentWeekAttendancePerformanceForProject(projectId);

        // Then
        assertNotNull(result);
        assertEquals("Project A", result.getProjectName());
        assertEquals(12, result.getTotalHours()); // 8 + 4 hours

        // Calculate expected hours for the week based on employment types
        var expectedHoursPerWeek = employee1.getEmploymentType().getExpectedHoursPerDay() * 5
                + employee2.getEmploymentType().getExpectedHoursPerDay() * 5;
        assertEquals(expectedHoursPerWeek, result.getExpectedHours());

        double averageHoursPerDay = (double) result.getTotalHours() / 5;
        assertEquals(averageHoursPerDay, result.getAverageHoursPerDay(), 0.01);

        int totalDaysInRange = (int) (Duration.between(startOfWeek.atStartOfDay(), endOfWeek.atStartOfDay()).toDays()) + 1;
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
        var startOfWeek = now().with(MONDAY);
        var today = now();
        
        var record1 = createAttendanceRecord(employee, project, startOfWeek, 9, 17);
        var record2 = createAttendanceRecord(employee, project, today, 8, 16);
        var records = List.of(record1, record2);

        when(attendanceRecordRepository.findByProjectIdAndDateBetween(projectId, startOfWeek, today))
                .thenReturn(records);

        // When
        var result = attendanceService.getAttendanceRecordsByProjectSinceStartOfWeek(projectId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetCurrentWeekAttendancePerformanceForProject_ProjectNotFound() {
        // Given
        var projectId = 999L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // When
        var result = attendanceService.getCurrentWeekAttendancePerformanceForProject(projectId);

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
}
