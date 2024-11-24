package cz.cvut.fel.pm2.timely_be.mapper;

import cz.cvut.fel.pm2.timely_be.dto.*;
import cz.cvut.fel.pm2.timely_be.model.*;

import java.util.stream.Collectors;

public class MapperUtils {
    public static EmployeeDto toEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getEmployeeId());
        employeeDto.setName(employee.getName());
        employeeDto.setJobTitle(employee.getJobTitle());
        employeeDto.setEmploymentStatus(employee.getEmploymentStatus().name());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPhoneNumber(employee.getPhoneNumber());
        employeeDto.setCurrentProjects(employee.getCurrentProjects().stream().map(Project::getName).collect(Collectors.toList()));

        employeeDto.setAnnualSalary(employee.getAnnualSalary());
        employeeDto.setAnnualLearningBudget(employee.getAnnualLearningBudget());
        employeeDto.setAnnualBusinessPerformanceBonusMax(employee.getAnnualBusinessPerformanceBonusMax());
        employeeDto.setAnnualPersonalPerformanceBonusMax(employee.getAnnualPersonalPerformanceBonusMax());

        return employeeDto;
    }

    public static TeamDTO toTeamDto(Team team) {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamId(team.getId());
        teamDTO.setName(team.getName());
        teamDTO.setManager(team.getManager().getName());
        teamDTO.setMembers(team.getMembers().stream().map(Employee::getName).collect(Collectors.toList()));
        return teamDTO;
    }

    public static AttendanceRecordDto toAttendanceRecordDto(AttendanceRecord attendance) {
        AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
        attendanceRecordDto.setAttendanceId(attendance.getAttendanceId());
        attendanceRecordDto.setMember(attendance.getMember().getName());
        attendanceRecordDto.setDate(attendance.getDate());
        attendanceRecordDto.setClockInTime(attendance.getClockInTime());
        attendanceRecordDto.setClockOutTime(attendance.getClockOutTime());
        attendanceRecordDto.setProject(attendance.getProject().getName());
        attendanceRecordDto.setDescription(attendance.getDescription());
        return attendanceRecordDto;
    }

    public static EmployeeNameWithIdDto toEmployeeNameWithIdDto(Employee employee) {
        EmployeeNameWithIdDto employeeNameWithIdDto = new EmployeeNameWithIdDto();
        employeeNameWithIdDto.setId(employee.getEmployeeId());
        employeeNameWithIdDto.setName(employee.getName());
        return employeeNameWithIdDto;
    }
    public static SubmissionDto toSubmissionDto(Submission submission, Employee employee) {
        SubmissionDto dto = new SubmissionDto();
        dto.setMessageId(submission.getMessageId());
        dto.setEmployee(toEmployeeNameWithIdDto(employee));
        dto.setDatetime(submission.getDatetime());
        dto.setStatus(submission.getStatus());
        dto.setMessage(submission.getMessage());
        return dto;
    }
}
