package cz.cvut.fel.pm2.timely_be.mapper;

import cz.cvut.fel.pm2.timely_be.dto.AttendanceRecordDto;
import cz.cvut.fel.pm2.timely_be.dto.EmployeeDto;
import cz.cvut.fel.pm2.timely_be.dto.EmployeeNameWithIdDto;
import cz.cvut.fel.pm2.timely_be.dto.TeamDTO;
import cz.cvut.fel.pm2.timely_be.model.AttendanceRecord;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.model.Team;

import java.util.stream.Collectors;

public class MapperUtils {
    public static EmployeeDto toDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName(employee.getName());
        employeeDto.setJobTitle(employee.getJobTitle());
        employeeDto.setEmploymentStatus(employee.getEmploymentStatus().name());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPhoneNumber(employee.getPhoneNumber());
        employeeDto.setCurrentProjects(employee.getCurrentProjects().stream().map(Project::getName).collect(Collectors.toList()));
        return employeeDto;
    }

    public static TeamDTO toDto(Team team) {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamId(team.getId());
        teamDTO.setName(team.getName());
        teamDTO.setManager(team.getManager().getName());
        teamDTO.setMembers(team.getMembers().stream().map(Employee::getName).collect(Collectors.toList()));
        return teamDTO;
    }

    public static AttendanceRecordDto toDto(AttendanceRecord attendance) {
        AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
        attendanceRecordDto.setMember(attendance.getMember().getName());
        attendanceRecordDto.setDate(attendance.getDate());
        attendanceRecordDto.setClockInTime(attendance.getClockInTime());
        attendanceRecordDto.setClockOutTime(attendance.getClockOutTime());
        attendanceRecordDto.setProject(attendance.getProject().getName());
        return attendanceRecordDto;
    }

    public static EmployeeNameWithIdDto toEmployeeNameWithIdDto(Employee employee) {
        EmployeeNameWithIdDto employeeNameWithIdDto = new EmployeeNameWithIdDto();
        employeeNameWithIdDto.setId(employee.getEmployeeId());
        employeeNameWithIdDto.setName(employee.getName());
        return employeeNameWithIdDto;
    }
}
