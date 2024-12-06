package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.repository.*;
import cz.cvut.fel.pm2.timely_be.service.UserService;
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

    private final LeaveRepository leaveRepository;
    private final EmployeeLeaveBalanceRepository leaveBalanceRepository;

    private final UserService userService;
    private final LearningRepository learningRepository;
    private final EmployeeLearningRepository employeeLearningRepository;


    public TestController(EmployeeRepository employeeRepository, TeamRepository teamRepository,
                          AttendanceRecordRepository attendanceRecordRepository, ProjectRepository projectRepository,
                          LeaveRepository leaveRepository, EmployeeLeaveBalanceRepository leaveBalanceRepository, UserService userService,
                          LearningRepository learningRepository, EmployeeLearningRepository employeeLearningRepository) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.leaveRepository = leaveRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.userService = userService;
        this.learningRepository = learningRepository;
        this.employeeLearningRepository = employeeLearningRepository;
    }
    @DeleteMapping("/resetToBaseState")
    @Operation(summary = "Reset database to base state", description = "Reset database to base state for testing purposes")
    public void resetToBaseState() {
        resetDatabaseToBaseState(employeeRepository, teamRepository, attendanceRecordRepository, projectRepository,
                leaveRepository, leaveBalanceRepository, userService, learningRepository, employeeLearningRepository);
    }
}
