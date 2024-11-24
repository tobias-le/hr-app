package cz.cvut.fel.pm2.timely_be.jobs;

import cz.cvut.fel.pm2.timely_be.config.DatabaseInitializer;
import cz.cvut.fel.pm2.timely_be.repository.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DatabaseResetJob {
    
    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final ProjectRepository projectRepository;
    private final LeaveRepository leaveRepository;
    private final EmployeeLeaveBalanceRepository leaveBalanceRepository;

    public DatabaseResetJob(
            EmployeeRepository employeeRepository,
            TeamRepository teamRepository,
            AttendanceRecordRepository attendanceRecordRepository,
            ProjectRepository projectRepository,
            LeaveRepository leaveRepository,
            EmployeeLeaveBalanceRepository leaveBalanceRepository) {
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.projectRepository = projectRepository;
        this.leaveRepository = leaveRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    @Scheduled(cron = "0 0 0 * * MON") // Runs at 00:00:00 every Monday
    public void resetDatabase() {
        log.info("Starting scheduled database reset");
        try {
            DatabaseInitializer.resetDatabaseToBaseState(
                employeeRepository,
                teamRepository,
                attendanceRecordRepository,
                projectRepository,
                leaveRepository,
                leaveBalanceRepository
            );
            log.info("Scheduled database reset completed successfully");
        } catch (Exception e) {
            log.error("Error during scheduled database reset", e);
        }
    }
} 