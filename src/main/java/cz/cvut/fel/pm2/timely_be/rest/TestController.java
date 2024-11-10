package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.repository.AttendanceRecordRepository;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.ProjectRepository;
import cz.cvut.fel.pm2.timely_be.repository.TeamRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cz.cvut.fel.pm2.timely_be.config.DatabaseInitializer.resetDatabaseToBaseState;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    public TestController(EmployeeRepository employeeRepository, ProjectRepository projectRepository,
                          TeamRepository teamRepository, AttendanceRecordRepository attendanceRecordRepository) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
    }
    @DeleteMapping("/resetToBaseState")
    @Operation(summary = "Reset database to base state", description = "Reset database to base state for testing purposes")
    public void resetToBaseState() {
        resetDatabaseToBaseState(employeeRepository, teamRepository, attendanceRecordRepository, projectRepository);
    }
}
