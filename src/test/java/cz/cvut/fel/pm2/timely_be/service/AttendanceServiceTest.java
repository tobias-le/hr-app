package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.model.AttendanceRecord;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Team;
import cz.cvut.fel.pm2.timely_be.repository.AttendanceRecordRepository;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.*;

import static cz.cvut.fel.pm2.timely_be.enums.EmploymentStatus.FULL_TIME;
import static cz.cvut.fel.pm2.timely_be.enums.EmploymentStatus.PART_TIME;
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
    private TeamRepository teamRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @BeforeEach
    public void setUp() {
        // Setup code if necessary
    }

    @Test
    public void testGetAttendanceRecordsByTeamSinceStartOfWeek() {
        // Given
        var employee1 = createEmployee(FULL_TIME);
        var record1 = createAttendanceRecord(employee1, employee1.getCurrentProjects().get(0));
        var record2 = createAttendanceRecord(employee1, employee1.getCurrentProjects().get(0));
        var records = Arrays.asList(record1, record2);

        when(attendanceRecordRepository.findByTeamIdAndDateBetween(eq(employee1.getTeam().getId()), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(records);

        // When
        var result = attendanceService.getAttendanceRecordsByTeamSinceStartOfWeek(employee1.getTeam().getId());

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAttendanceRecordsByMember() {
        // Given
        var member = createEmployee(FULL_TIME);
        var record1 = createAttendanceRecord(member, member.getCurrentProjects().get(0));
        var record2 = createAttendanceRecord(member, member.getCurrentProjects().get(0));
        var records = Arrays.asList(record1, record2);

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
        var employee1 = createEmployee(FULL_TIME);
        var record = createAttendanceRecord(employee1, employee1.getCurrentProjects().get(0));
        record.setAttendanceId(attendanceId);

        when(attendanceRecordRepository.findById(attendanceId)).thenReturn(Optional.of(record));

        // When
        var result = attendanceService.getAttendanceRecordById(attendanceId);

        // Then
        assertTrue(result.isPresent());
    }

    @Test
    public void testGetCurrentWeekAttendancePerformance() {
        // Given
        var teamId = 1L;
        var team = new Team();
        team.setId(teamId);
        team.setName("Development Team");

        var employee1 = new Employee();
        employee1.setEmployeeId(1L);
        employee1.setEmploymentStatus(FULL_TIME);

        var employee2 = createEmployee(PART_TIME);
        employee2.setEmployeeId(2L);
        employee2.setEmploymentStatus(PART_TIME);

        team.setMembers(Arrays.asList(employee1, employee2));

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        var startOfWeek = now().with(MONDAY);
        var endOfWeek = now().with(SUNDAY);

        // Create AttendanceRecords with LocalDateTime for clockInTime and clockOutTime
        var record1 = new AttendanceRecord();
        record1.setMember(employee1);
        record1.setDate(startOfWeek);
        record1.setClockInTime(LocalDateTime.of(startOfWeek, LocalTime.of(9, 0)));
        record1.setClockOutTime(LocalDateTime.of(startOfWeek, LocalTime.of(17, 0))); // 8 hours

        var record2 = new AttendanceRecord();
        record2.setMember(employee2);
        record2.setDate(startOfWeek.plusDays(1));
        record2.setClockInTime(LocalDateTime.of(startOfWeek.plusDays(1), LocalTime.of(10, 0)));
        record2.setClockOutTime(LocalDateTime.of(startOfWeek.plusDays(1), LocalTime.of(14, 0))); // 4 hours

        var records = Arrays.asList(record1, record2);

        when(attendanceRecordRepository.findByTeamAndDateBetween(eq(team), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(records);

        when(employeeRepository.findByTeamId(teamId)).thenReturn(Arrays.asList(employee1, employee2));

        // When
        var result = attendanceService.getCurrentWeekAttendancePerformance(teamId);

        // Then
        assertNotNull(result);
        assertEquals("Development Team", result.getTeamName());
        assertEquals(12, result.getTotalHours()); // 8 + 4 hours

        // Calculate expected hours for team per week
        var expectedHoursPerWeek = (FULL_TIME.getExpectedHoursPerDay() * 5)
                + (PART_TIME.getExpectedHoursPerDay() * 5);

        assertEquals(expectedHoursPerWeek, result.getExpectedHours());

        var averageHoursPerDay = (double) result.getTotalHours() / 5;
        assertEquals(averageHoursPerDay, result.getAverageHoursPerDay(), 0.01);

        var totalDaysInRange = (int) (Duration.between(startOfWeek.atStartOfDay(), endOfWeek.plusDays(1).atStartOfDay()).toDays());
        var attendanceRate = (double) records.size() / (team.getMembers().size() * totalDaysInRange) * 100;
        assertEquals(attendanceRate, result.getAttendanceRate(), 0.01);
    }
}
