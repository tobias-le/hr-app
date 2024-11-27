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
}
