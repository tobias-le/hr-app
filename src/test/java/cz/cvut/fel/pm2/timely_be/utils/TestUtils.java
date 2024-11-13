package cz.cvut.fel.pm2.timely_be.utils;

import cz.cvut.fel.pm2.timely_be.dto.EmployeeDto;
import cz.cvut.fel.pm2.timely_be.enums.EmploymentType;
import cz.cvut.fel.pm2.timely_be.model.AttendanceRecord;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.model.Team;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TestUtils {
    public static long getRandomId() {
        return (long) (Math.random() * 1000);
    }
    public static Employee createEmployee(EmploymentType employmentType) {
        var employee = new Employee();
        var id = getRandomId();
        employee.setEmployeeId(id);
        employee.setName("Employee " + id);
        employee.setJobTitle("Job Title " + id);
        employee.setEmail("employee" + id + "@example.com");
        employee.setPhoneNumber("123456789");
        employee.setEmploymentType(employmentType);

        var project = createProject();
        employee.setCurrentProjects(List.of(project));

        var team = createTeam(List.of(employee));
        employee.setTeam(team);

        return employee;
    }

    public static Team createTeam(List<Employee> members) {
        var id = getRandomId();
        Team team = new Team();
        team.setId(id);
        team.setName("Team " + id);
        team.setMembers(members);
        team.setManager(members.get(0));
        return team;
    }

    public static Project createProject() {
        var id = getRandomId();
        Project project = new Project();
        project.setProjectId(id);
        project.setName("Project " + id);
        return project;
    }

    public static AttendanceRecord createAttendanceRecord(Employee employee, Project project) {
        var id = getRandomId();
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setAttendanceId(id);
        attendanceRecord.setMember(employee);
        attendanceRecord.setProject(project);
        attendanceRecord.setDate(LocalDate.now());
        attendanceRecord.setClockInTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));
        attendanceRecord.setClockOutTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)));
        return attendanceRecord;
    }

    public static AttendanceRecord createAttendanceRecord(Employee employee, Project project, LocalDate date, int clockInHour, int clockOutHour) {
        var id = getRandomId();
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setAttendanceId(id);
        attendanceRecord.setMember(employee);
        attendanceRecord.setProject(project);
        attendanceRecord.setDate(date);
        attendanceRecord.setClockInTime(LocalDateTime.of(date, LocalTime.of(clockInHour, 0)));
        attendanceRecord.setClockOutTime(LocalDateTime.of(date, LocalTime.of(clockOutHour, 0)));
        return attendanceRecord;
    }


    public static EmployeeDto createEmployeeDto(EmploymentType employmentType) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName("Employee");
        employeeDto.setJobTitle("Job Title");
        employeeDto.setEmail("random"+ getRandomId() + "@example.com");
        employeeDto.setPhoneNumber("123456789");
        employeeDto.setEmploymentStatus(employmentType.toString());
        return employeeDto;
    }
}
