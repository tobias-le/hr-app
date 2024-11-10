package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.AttendanceRecordDto;
import cz.cvut.fel.pm2.timely_be.dto.AttendanceSummaryDTO;
import cz.cvut.fel.pm2.timely_be.enums.EmploymentStatus;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.AttendanceRecord;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Team;
import cz.cvut.fel.pm2.timely_be.repository.AttendanceRecordRepository;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.*;
import static java.time.LocalDate.now;

@Service
public class AttendanceService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public AttendanceService(AttendanceRecordRepository attendanceRecordRepository, TeamRepository teamRepository,
                             EmployeeRepository employeeRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.teamRepository = teamRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<AttendanceRecordDto> getAttendanceRecordsByTeamSinceStartOfWeek(Long teamId) {
        LocalDate startOfWeek = getStartOfWeek();
        LocalDate today = getToday();

        // Fetch attendance records for the team from the start of the week until today
        return attendanceRecordRepository
                .findByTeamIdAndDateBetween(teamId, startOfWeek, today)
                .stream()
                .map(MapperUtils::toAttendanceRecordDto)
                .collect(Collectors.toList());
    }

    // Method to find attendance records for a specific member
    public List<AttendanceRecordDto> getAttendanceRecordsByMember(Employee member) {
        return attendanceRecordRepository
                .findByMember(member)
                .stream()
                .map(MapperUtils::toAttendanceRecordDto)
                .collect(Collectors.toList());
    }

    // Method to find a specific attendance record by its ID
    public Optional<AttendanceRecordDto> getAttendanceRecordById(Long attendanceId) {
        return attendanceRecordRepository
                .findById(attendanceId)
                .map(MapperUtils::toAttendanceRecordDto);
    }

    public AttendanceSummaryDTO getCurrentWeekAttendancePerformance(Long teamId) {
        // Determine the current date
        Team team = teamRepository.findById(teamId).orElse(null);

        var startOfWeek = getStartOfWeek();
        var endOfWeek = getEndOfWeek();


        // Fetch attendance records for the team within the current work week
        List<AttendanceRecord> records = attendanceRecordRepository.findByTeamAndDateBetween(team, startOfWeek, endOfWeek);

        // Group records by date to determine attendance rate
        Map<LocalDate, List<AttendanceRecord>> recordsByDate = records.stream()
                .collect(Collectors.groupingBy(AttendanceRecord::getDate));

        // Calculate total hours worked and number of present days per employee
        long totalHours = 0;
        int totalDaysPresent = 0;

        for (Map.Entry<LocalDate, List<AttendanceRecord>> entry : recordsByDate.entrySet()) {
            List<AttendanceRecord> dailyRecords = entry.getValue();

            totalDaysPresent += dailyRecords.size();

            for (AttendanceRecord record : dailyRecords) {
                if (record.getClockInTime() != null && record.getClockOutTime() != null) {
                    Duration duration = Duration.between(record.getClockInTime(), record.getClockOutTime());
                    totalHours += duration.toHours();
                }
            }
        }

        // Calculate average hours per day and attendance rate
        int totalDaysInRange = (int) (Duration.between(startOfWeek.atStartOfDay(), endOfWeek.atStartOfDay()).toDays()) + 1;
        double averageHoursPerDay = (double) totalHours / 5;
        double attendanceRate;
        assert team != null;
        if (!team.getMembers().isEmpty()) {
            attendanceRate = totalDaysPresent / (double) (team.getMembers().size() * totalDaysInRange) * 100;
        } else {
            attendanceRate = 0;
        }

        // Create and return the DTO with the performance summary for the current work week
        return new AttendanceSummaryDTO(
                team.getName(),
                totalHours,
                getExpectedHoursForTeamPerWeek(teamId),
                averageHoursPerDay,
                attendanceRate
        );
    }

    private long getExpectedHoursForTeamPerWeek(Long teamId) {
        var employees = employeeRepository.findByTeamId(teamId);
        return employees.stream()
                .map(Employee::getEmploymentStatus)
                .map(EmploymentStatus::getExpectedHoursPerDay)
                .map(hours -> hours * 5)
                .reduce(Long::sum).orElse(0L);
    }

    public AttendanceRecord createAttendanceRecord(AttendanceRecordDto attendanceRecordDto) {
        var attendanceRecord = new AttendanceRecord();
        var employee = employeeRepository.findById(attendanceRecordDto.getMemberId()).orElseThrow();
        var projects = employee.getCurrentProjects();
        attendanceRecord.setMember(employee);

        attendanceRecord.setProject(
                projects.stream()
                        .filter(project -> project.getName().equals(attendanceRecordDto.getProject()))
                        .findFirst()
                        .orElseThrow()
        );
        attendanceRecord.setDate(attendanceRecordDto.getDate());
        attendanceRecord.setClockInTime(attendanceRecordDto.getClockInTime());
        attendanceRecord.setClockOutTime(attendanceRecordDto.getClockOutTime());
        return attendanceRecordRepository.save(attendanceRecord);
    }

    private LocalDate getStartOfWeek() {
        var today = now();
        return today.with(MONDAY);
    }

    private LocalDate getEndOfWeek() {
        var today = now();
        return today.with(SUNDAY);
    }

    private LocalDate getToday() {
        return now();
    }
}
