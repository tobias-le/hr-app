package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.AttendanceRecordDto;
import cz.cvut.fel.pm2.timely_be.dto.AttendanceSummaryDTO;
import cz.cvut.fel.pm2.timely_be.enums.EmploymentType;
import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.AttendanceRecord;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.model.Team;
import cz.cvut.fel.pm2.timely_be.repository.AttendanceRecordRepository;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.ProjectRepository;
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
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public AttendanceService(AttendanceRecordRepository attendanceRecordRepository,
                             EmployeeRepository employeeRepository, ProjectRepository projectRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    public List<AttendanceRecordDto> getAttendanceRecordsByProjectSinceStartOfWeek(Long projectId) {
        LocalDate startOfWeek = getStartOfWeek();
        LocalDate today = getToday();

        return attendanceRecordRepository
                .findByProjectIdAndDateBetween(projectId, startOfWeek, today)
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

    public AttendanceSummaryDTO getCurrentWeekAttendancePerformanceForProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) return null;
        return calculateAttendancePerformance(null, project);
    }

    private AttendanceSummaryDTO calculateAttendancePerformance(Team team, Project project) {
        var startOfWeek = getStartOfWeek();
        var endOfWeek = getEndOfWeek();

        List<AttendanceRecord> records;
        if (team != null) {
            records = attendanceRecordRepository.findByTeamAndDateBetween(team, startOfWeek, endOfWeek);
        } else if (project != null) {
            records = attendanceRecordRepository.findByProjectIdAndDateBetween(project.getProjectId(), startOfWeek, endOfWeek);
        } else {
            return null;
        }

        // Group records by date to determine attendance rate
        Map<LocalDate, List<AttendanceRecord>> recordsByDate = records.stream()
                .collect(Collectors.groupingBy(AttendanceRecord::getDate));

        // Calculate total hours worked and number of present days per employee
        long totalHours = 0;
        int totalDaysPresent = 0;

        for (List<AttendanceRecord> dailyRecords : recordsByDate.values()) {
            totalDaysPresent += dailyRecords.size();
            totalHours += dailyRecords.stream()
                    .filter(record -> record.getClockInTime() != null && record.getClockOutTime() != null)
                    .mapToLong(record -> Duration.between(record.getClockInTime(), record.getClockOutTime()).toHours())
                    .sum();
        }

        // Calculate average hours per day and attendance rate
        int totalDaysInRange = (int) (Duration.between(startOfWeek.atStartOfDay(), endOfWeek.atStartOfDay()).toDays()) + 1;
        double averageHoursPerDay = (double) totalHours / 5;

        int memberCount = team != null ? team.getMembers().size() : project.getMembers().size();
        double attendanceRate = memberCount > 0
                ? totalDaysPresent / (double) (memberCount * totalDaysInRange) * 100
                : 0;

        String name = team != null ? team.getName() : project.getName();
        Long entityId = team != null ? team.getId() : project.getProjectId();

        return new AttendanceSummaryDTO(
                name,
                totalHours,
                getExpectedHoursForEntityPerWeek(entityId, team != null),
                averageHoursPerDay,
                attendanceRate
        );
    }

    private long getExpectedHoursForEntityPerWeek(Long entityId, boolean isTeam) {
        List<Employee> employees;
        if (isTeam) {
            employees = employeeRepository.findByTeamId(entityId);
        } else {
            employees = employeeRepository.findEmployeesByProjectId(entityId);
        }
        return getExpectedHoursPerWeek(employees);
    }


    private long getExpectedHoursPerWeek(List<Employee> employees) {
        return employees.stream()
                .map(Employee::getEmploymentType)
                .map(EmploymentType::getExpectedHoursPerDay)
                .map(hours -> hours * 5)
                .reduce(Long::sum).orElse(0L);
    }

    public AttendanceRecord createAttendanceRecord(AttendanceRecordDto attendanceRecordDto) {
        var attendanceRecord = new AttendanceRecord();
        return getAttendanceRecord(attendanceRecordDto, attendanceRecord);
    }

    public AttendanceRecord updateAttendanceRecordById(Long id, AttendanceRecordDto attendanceRecordDto) {
        var attendanceRecord = attendanceRecordRepository.findById(id).orElseThrow();
        return getAttendanceRecord(attendanceRecordDto, attendanceRecord);
    }

    private AttendanceRecord getAttendanceRecord(AttendanceRecordDto attendanceRecordDto, AttendanceRecord attendanceRecord) {
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
        attendanceRecord.setStatus(RequestStatus.requestStatusFromString(attendanceRecordDto.getStatus()));
        return attendanceRecordRepository.save(attendanceRecord);
    }

    public void deleteAttendanceRecordById(Long id) {
        attendanceRecordRepository.deleteById(id);
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
