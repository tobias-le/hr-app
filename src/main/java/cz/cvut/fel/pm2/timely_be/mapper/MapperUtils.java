package cz.cvut.fel.pm2.timely_be.mapper;

import cz.cvut.fel.pm2.timely_be.dto.*;
import cz.cvut.fel.pm2.timely_be.model.AttendanceRecord;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.model.Team;
import org.hibernate.Hibernate;
import cz.cvut.fel.pm2.timely_be.model.*;

import java.util.stream.Collectors;

public class MapperUtils {
    public static EmployeeDto toEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getEmployeeId());
        employeeDto.setName(employee.getName());
        employeeDto.setJobTitle(employee.getJobTitle());
        employeeDto.setEmploymentStatus(employee.getEmploymentType().name());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPhoneNumber(employee.getPhoneNumber());
        employeeDto.setCurrentProjects(employee.getCurrentProjects().stream().map(Project::getName).collect(Collectors.toList()));

        employeeDto.setAnnualSalary(employee.getAnnualSalary());
        employeeDto.setAnnualLearningBudget(employee.getAnnualLearningBudget());
        employeeDto.setAnnualBusinessPerformanceBonusMax(employee.getAnnualBusinessPerformanceBonusMax());
        employeeDto.setAnnualPersonalPerformanceBonusMax(employee.getAnnualPersonalPerformanceBonusMax());
        employeeDto.setHR(employee.isHR());

        employeeDto.setDateOfBirth(employee.getDateOfBirth());
        employeeDto.setInternationalBankAccountNumber(employee.getInternationalBankAccountNumber());
        return employeeDto;
    }

    public static TeamDTO toTeamDto(Team team) {
        if (team == null) return null;

        TeamDTO dto = new TeamDTO();
        dto.setTeamId(team.getId());
        dto.setName(team.getName());

        if (team.getManager() != null) {
            dto.setManagerId(team.getManager().getEmployeeId());
            dto.setManagerName(team.getManager().getName());
        }

        if (team.getMembers() != null) {
            dto.setMembers(team.getMembers().stream()
                .map(MapperUtils::toEmployeeDto)
                .collect(Collectors.toSet()));
        }

        return dto;
    }

    public static TeamDTO toTeamDtoWithHierarchy(Team team) {
        if (team == null) return null;

        TeamDTO dto = new TeamDTO();
        dto.setTeamId(team.getId());
        dto.setName(team.getName());

        if (team.getManager() != null) {
            dto.setManagerId(team.getManager().getEmployeeId());
            dto.setManagerName(team.getManager().getName());
            dto.setManagerJobTitle(team.getManager().getJobTitle());
        }

        if (team.getMembers() != null) {
            dto.setMembers(team.getMembers().stream()
                .map(MapperUtils::toEmployeeDto)
                .collect(Collectors.toSet()));
        }

        if (team.getParentTeam() != null) {
            dto.setParentTeam(toTeamDtoWithHierarchy(team.getParentTeam()));
        }

        return dto;
    }

    public static AttendanceRecordDto toAttendanceRecordDto(AttendanceRecord attendance) {
        AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
        attendanceRecordDto.setAttendanceId(attendance.getAttendanceId());
        attendanceRecordDto.setMember(attendance.getMember().getName());
        attendanceRecordDto.setMemberId(attendance.getMember().getEmployeeId());
        attendanceRecordDto.setDate(attendance.getDate());
        attendanceRecordDto.setClockInTime(attendance.getClockInTime());
        attendanceRecordDto.setClockOutTime(attendance.getClockOutTime());
        attendanceRecordDto.setProject(attendance.getProject().getName());
        attendanceRecordDto.setDescription(attendance.getDescription());
        attendanceRecordDto.setStatus(attendance.getStatus().name());
        return attendanceRecordDto;
    }

    public static ProjectDto toProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProjectId(project.getProjectId());
        projectDto.setName(project.getName());
        projectDto.setManagerName(project.getManager().getName());
        projectDto.setManagerId(project.getManager().getEmployeeId());

        if (Hibernate.isInitialized(project.getMembers())) {
            projectDto.setMembers(
                project.getMembers()
                    .stream()
                    .map(MapperUtils::toEmployeeDto)
                    .collect(Collectors.toSet())
            );
        }
        return projectDto;
    }

    public static LearningDto toLearningDto(Learning learning) {
        LearningDto learningDto = new LearningDto();
        learningDto.setLearningId(learning.getLearningId());
        learningDto.setName(learning.getName());
        learningDto.setLink(learning.getLink());
        return learningDto;
    }
    public static EmployeeNameWithIdDto toEmployeeNameWithIdDto(Employee employee) {
        return new EmployeeNameWithIdDto(employee.getEmployeeId(), employee.getName());
    }

    public static SubmissionDto toSubmissionDto(Submission submission, EmployeeNameWithIdDto employee) {
        SubmissionDto dto = new SubmissionDto();
        dto.setMessageId(submission.getMessageId());
        dto.setEmployee(employee);
        dto.setDatetime(submission.getDatetime());
        dto.setMessage(submission.getMessage());
        dto.setStatus(submission.getStatus().name());
        return dto;
    }

    public static LeaveRequestDto leaveRequestDto(Leave leave, Employee employee, EmployeeLeaveBalance balance) {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setEmployee(MapperUtils.toEmployeeNameWithIdDto(employee));
        dto.setLeaveAmount(leave.getLeaveAmount());
        dto.setLeaveType(leave.getLeaveType());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setMessage(leave.getReason());
        dto.setStatus(leave.getStatus().toString());
        dto.setDatetime(leave.getDatetime());
        dto.setMessageId(leave.getId());
        int days = 0;
        switch (leave.getLeaveType()){
            case SICK_LEAVE ->  days = balance.getSickDaysLeft();
            case VACATION_LEAVE -> days = balance.getVacationDaysLeft();
            case PERSONAL_LEAVE -> days = balance.getPersonalDaysLeft();
        }
        dto.setCurrentDaysLeft(days);
        return dto;
    }
}
