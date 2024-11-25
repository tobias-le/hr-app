package cz.cvut.fel.pm2.timely_be.config;

import cz.cvut.fel.pm2.timely_be.repository.*;
import cz.cvut.fel.pm2.timely_be.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@Slf4j
public class WeeklyDataScheduler {

    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final ProjectRepository projectRepository;
    private final LeaveRepository leaveRepository;
    private final EmployeeLeaveBalanceRepository leaveBalanceRepository;
    private final UserService userService;
    private final LearningRepository learningRepository;
    private final EmployeeLearningRepository employeeLearningRepository;

    @Autowired
    public WeeklyDataScheduler(EmployeeRepository employeeRepository, TeamRepository teamRepository,
                              AttendanceRecordRepository attendanceRecordRepository, ProjectRepository projectRepository,
                              LeaveRepository leaveRepository, EmployeeLeaveBalanceRepository leaveBalanceRepository,
                              UserService userService, LearningRepository learningRepository,
                              EmployeeLearningRepository employeeLearningRepository) {
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.projectRepository = projectRepository;
        this.leaveRepository = leaveRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.userService = userService;
        this.learningRepository = learningRepository;
        this.employeeLearningRepository = employeeLearningRepository;
    }

    // Run at 00:00 on Monday
    @Scheduled(cron = "0 0 0 * * MON")
    @Transactional
    public void scheduleWeeklyDataLoad() {
        try {
            log.info("Starting weekly data initialization at {}", LocalDateTime.now());
            DatabaseInitializer.resetDatabaseToBaseState(
                employeeRepository, teamRepository, attendanceRecordRepository,
                projectRepository, leaveRepository, leaveBalanceRepository,
                userService, learningRepository, employeeLearningRepository
            );
            log.info("Completed weekly data initialization at {}", LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error during weekly data initialization: ", e);
        }
    }
} 