package cz.cvut.fel.pm2.timely_be.mapper;

import cz.cvut.fel.pm2.timely_be.dto.*;
import cz.cvut.fel.pm2.timely_be.model.AttendanceRecord;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.model.Team;
import org.hibernate.Hibernate;

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
                    .toList()
            );
        }
        return projectDto;
    }
}
