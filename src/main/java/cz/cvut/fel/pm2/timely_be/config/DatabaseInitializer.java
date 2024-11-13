package cz.cvut.fel.pm2.timely_be.config;

import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import cz.cvut.fel.pm2.timely_be.model.*;
import cz.cvut.fel.pm2.timely_be.enums.EmploymentType;
import cz.cvut.fel.pm2.timely_be.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Collections;

import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.FULL_TIME;
import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.PART_TIME;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initEmployeeDatabase(EmployeeRepository employeeRepository, TeamRepository teamRepository
            , AttendanceRecordRepository attendanceRecordRepository, ProjectRepository projectRepository, LeaveRepository leaveRepository, EmployeeLeaveBalanceRepository leaveBalanceRepository) {
        return args -> resetDatabaseToBaseState(employeeRepository, teamRepository, attendanceRecordRepository, projectRepository, leaveRepository, leaveBalanceRepository);
    }

    public static void resetDatabaseToBaseState(EmployeeRepository employeeRepository, TeamRepository teamRepository,
            AttendanceRecordRepository attendanceRecordRepository, ProjectRepository projectRepository,
            LeaveRepository leaveRepository, EmployeeLeaveBalanceRepository leaveBalanceRepository) {
        
        // Initialize base data in correct order
        var projects = initializeProjects(projectRepository);
        var employees = initializeEmployees(employeeRepository, Collections.emptyList());
        
        // Update projects with managers and save
        updateProjectManagers(projectRepository, projects, employees);
        
        // Randomly assign 1-4 projects to each employee
        employees.forEach(employee -> {
            Collections.shuffle(projects);
            int numProjects = 1 + random.nextInt(4); // Random number between 1 and 4
            var assignedProjects = new ArrayList<>(projects.subList(0, numProjects));
            employee.setCurrentProjects(assignedProjects);
        });
        
        // Initialize teams and assign employees to teams
        var teams = initializeTeams(teamRepository, employees);
        
        // Save employees with updated projects and team assignments
        employeeRepository.saveAll(employees);
        
        // Initialize attendance records
        initializeAttendanceRecords(attendanceRecordRepository, employees, projects);
        
        // Initialize leave balances and requests
        initializeLeaveData(leaveRepository, leaveBalanceRepository, employees);
    }

    private static List<Project> initializeProjects(ProjectRepository projectRepository) {
        // Strategic Projects
        var digitalTransformation = createProject("Digital Transformation Initiative");
        var marketExpansion = createProject("Global Market Expansion");
        var sustainabilityProgram = createProject("Corporate Sustainability Program");
        
        // Infrastructure Projects
        var cloudMigration = createProject("Cloud Infrastructure Migration");
        var securityOverhaul = createProject("Security Systems Overhaul");
        var networkUpgrade = createProject("Network Infrastructure Upgrade");
        
        // Product Development
        var mobileApp = createProject("Mobile App Development");
        var webPlatform = createProject("Web Platform Redesign");
        var aiIntegration = createProject("AI Integration Initiative");
        
        // Business Operations
        var erpImplementation = createProject("ERP System Implementation");
        var crmUpgrade = createProject("CRM System Upgrade");
        var analyticsPlaftorm = createProject("Analytics Platform Development");

        return Arrays.asList(
            digitalTransformation, marketExpansion, sustainabilityProgram,
            cloudMigration, securityOverhaul, networkUpgrade,
            mobileApp, webPlatform, aiIntegration,
            erpImplementation, crmUpgrade, analyticsPlaftorm
        );
    }

    private static List<Employee> initializeEmployees(EmployeeRepository employeeRepository, List<Project> projects) {
        List<Employee> allEmployees = new ArrayList<>();
        
        // C-Suite
        allEmployees.addAll(Arrays.asList(
            createEmployee("John Smith", "Chief Executive Officer", FULL_TIME, "john.smith@company.com", projects),
            createEmployee("Sarah Johnson", "Chief Technology Officer", FULL_TIME, "sarah.johnson@company.com", projects),
            createEmployee("Michael Chen", "Chief Financial Officer", FULL_TIME, "michael.chen@company.com", projects),
            createEmployee("Emma Davis", "Chief Operations Officer", FULL_TIME, "emma.davis@company.com", projects)
        ));

        // Infrastructure Department
        allEmployees.addAll(Arrays.asList(
            createEmployee("David Wilson", "Infrastructure Manager", FULL_TIME, "david.wilson@company.com", projects),
            createEmployee("Lisa Brown", "Senior Network Engineer", FULL_TIME, "lisa.brown@company.com", projects),
            createEmployee("James Taylor", "Cloud Architect", FULL_TIME, "james.taylor@company.com", projects),
            createEmployee("Anna Martinez", "Security Specialist", FULL_TIME, "anna.martinez@company.com", projects),
            createEmployee("Robert Lee", "System Administrator", FULL_TIME, "robert.lee@company.com", projects)
        ));

        // Development Department
        allEmployees.addAll(Arrays.asList(
            createEmployee("Patricia White", "Development Director", FULL_TIME, "patricia.white@company.com", projects),
            createEmployee("Thomas Anderson", "Senior Software Engineer", FULL_TIME, "thomas.anderson@company.com", projects),
            createEmployee("Maria Garcia", "Frontend Developer", FULL_TIME, "maria.garcia@company.com", projects),
            createEmployee("Steven Wright", "Backend Developer", FULL_TIME, "steven.wright@company.com", projects),
            createEmployee("Jennifer Kim", "QA Engineer", FULL_TIME, "jennifer.kim@company.com", projects),
            createEmployee("Kevin Patel", "DevOps Engineer", FULL_TIME, "kevin.patel@company.com", projects)
        ));

        // Finance Department
        allEmployees.addAll(Arrays.asList(
            createEmployee("Richard Moore", "Finance Director", FULL_TIME, "richard.moore@company.com", projects),
            createEmployee("Susan Miller", "Senior Accountant", FULL_TIME, "susan.miller@company.com", projects),
            createEmployee("Daniel Clark", "Financial Analyst", FULL_TIME, "daniel.clark@company.com", projects),
            createEmployee("Laura Thompson", "Payroll Specialist", PART_TIME, "laura.thompson@company.com", projects)
        ));

        // HR Department
        allEmployees.addAll(Arrays.asList(
            createEmployee("Catherine Adams", "HR Director", FULL_TIME, "catherine.adams@company.com", projects),
            createEmployee("Paul Roberts", "HR Manager", FULL_TIME, "paul.roberts@company.com", projects),
            createEmployee("Nancy Turner", "Recruitment Specialist", PART_TIME, "nancy.turner@company.com", projects),
            createEmployee("George Harris", "Training Coordinator", FULL_TIME, "george.harris@company.com", projects)
        ));

        return employeeRepository.saveAll(allEmployees);
    }

    private static List<Team> initializeTeams(TeamRepository teamRepository, List<Employee> employees) {
        // Find managers
        Employee infraManager = findEmployeeByTitle(employees, "Infrastructure Manager");
        Employee devManager = findEmployeeByTitle(employees, "Development Director");
        Employee finManager = findEmployeeByTitle(employees, "Finance Director");
        Employee hrManager = findEmployeeByTitle(employees, "HR Director");

        // Create teams
        Team infrastructureTeam = createTeam("Infrastructure Team", infraManager);
        Team developmentTeam = createTeam("Development Team", devManager);
        Team financeTeam = createTeam("Finance Team", finManager);
        Team hrTeam = createTeam("HR Team", hrManager);

        // Save teams first
        List<Team> teams = teamRepository.saveAll(Arrays.asList(infrastructureTeam, developmentTeam, financeTeam, hrTeam));

        // Assign team members and update employees
        for (Employee employee : employees) {
            String title = employee.getJobTitle().toLowerCase();
            
            if (title.contains("network") || title.contains("cloud") || 
                title.contains("security") || title.contains("system")) {
                infrastructureTeam.addMember(employee);
                employee.setTeam(infrastructureTeam);
            }
            else if (title.contains("developer") || title.contains("engineer") || 
                     title.contains("qa") || title.contains("devops")) {
                developmentTeam.addMember(employee);
                employee.setTeam(developmentTeam);
            }
            else if (title.contains("accountant") || title.contains("financial") || 
                     title.contains("payroll")) {
                financeTeam.addMember(employee);
                employee.setTeam(financeTeam);
            }
            else if (title.contains("recruitment") || title.contains("training")) {
                hrTeam.addMember(employee);
                employee.setTeam(hrTeam);
            }
        }

        // Save updated teams and employees
        teamRepository.saveAll(teams);
        return teams;
    }

    private static Employee findEmployeeByTitle(List<Employee> employees, String title) {
        return employees.stream()
                .filter(e -> e.getJobTitle().equals(title))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee with title " + title + " not found"));
    }

    private static void initializeAttendanceRecords(AttendanceRecordRepository attendanceRecordRepository,
                                                  List<Employee> employees, List<Project> projects) {
        List<AttendanceRecord> records = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        for (Employee employee : employees) {
            List<Project> employeeProjects = employee.getCurrentProjects();
            if (employeeProjects.isEmpty()) continue;
            
            for (int i = 0; i < 5; i++) {  // Last 5 days
                LocalDate date = today.minusDays(i);
                // Pick a random project from employee's assigned projects
                Project randomProject = employeeProjects.get(random.nextInt(employeeProjects.size()));
                
                AttendanceRecord record = new AttendanceRecord();
                record.setMember(employee);
                record.setProject(randomProject);
                record.setDate(date);
                record.setClockInTime(LocalDateTime.of(date, LocalTime.of(8 + random.nextInt(2), random.nextInt(60))));
                record.setClockOutTime(LocalDateTime.of(date, LocalTime.of(16 + random.nextInt(3), random.nextInt(60))));
                record.setDescription("Work on " + randomProject.getName());
                record.setStatus(getRandomStatus());
                record.setDeleted(false);
                
                records.add(record);
            }
        }
        
        attendanceRecordRepository.saveAll(records);
    }

    private static void initializeLeaveData(LeaveRepository leaveRepository, 
            EmployeeLeaveBalanceRepository leaveBalanceRepository, List<Employee> employees) {
        // Initialize leave balances for all employees
        List<EmployeeLeaveBalance> balances = employees.stream()
            .map(employee -> createLeaveBalance(employee.getEmployeeId()))
            .collect(Collectors.toList());
        leaveBalanceRepository.saveAll(balances);

        // Create some sample leave requests
        List<Leave> leaveRequests = new ArrayList<>();
        for (Employee employee : employees) {
            if (random.nextBoolean()) { // 50% chance for each employee to have a leave request
                leaveRequests.add(createLeaveRequest(employee.getEmployeeId()));
            }
        }
        leaveRepository.saveAll(leaveRequests);
    }

    // Helper methods
    private static Project createProject(String name) {
        var project = new Project();
        project.setName(name);
        return project;
    }

    private static Employee createEmployee(String name, String jobTitle, EmploymentType type, 
            String email, List<Project> projects) {
        var employee = new Employee();
        employee.setName(name);
        employee.setJobTitle(jobTitle);
        employee.setEmploymentType(type);
        employee.setEmail(email);
        employee.setPhoneNumber(generatePhoneNumber());
        employee.setCurrentProjects(new ArrayList<>(projects));
        return employee;
    }

    private static Team createTeam(String name, Employee manager) {
        var team = new Team();
        team.setName(name);
        team.setManager(manager);
        team.setMembers(new ArrayList<>());
        return team;
    }

    private static final Random random = new Random();

    private static String generatePhoneNumber() {
        return String.format("%09d", random.nextInt(1000000000));
    }

    private static Project getRandomProject(List<Project> projects) {
        return projects.get(random.nextInt(projects.size()));
    }

    private static RequestStatus getRandomStatus() {
        return RequestStatus.values()[random.nextInt(RequestStatus.values().length)];
    }

    private static AttendanceRecord createAttendanceRecord(Employee employee, Project project, 
            LocalDate date, LocalDateTime clockIn, LocalDateTime clockOut, RequestStatus status) {
        var record = new AttendanceRecord();
        record.setMember(employee);
        record.setProject(project);
        record.setDate(date);
        record.setClockInTime(clockIn);
        record.setClockOutTime(clockOut);
        record.setStatus(status);
        return record;
    }

    private static EmployeeLeaveBalance createLeaveBalance(Long employeeId) {
        var balance = new EmployeeLeaveBalance();
        balance.setEmployeeId(employeeId);
        balance.setVacationDaysLeft(20 + random.nextInt(5));
        balance.setSickDaysLeft(10 + random.nextInt(3));
        balance.setPersonalDaysLeft(5 + random.nextInt(2));
        return balance;
    }

    private static Leave createLeaveRequest(Long employeeId) {
        var leave = new Leave();
        leave.setEmployeeId(employeeId);
        leave.setLeaveType(LeaveType.values()[random.nextInt(LeaveType.values().length)]);
        
        LocalDate startDate = LocalDate.now().plusDays(random.nextInt(30));
        int duration = 1 + random.nextInt(5);
        leave.setStartDate(startDate);
        leave.setEndDate(startDate.plusDays(duration));
        leave.setLeaveAmount(duration);
        leave.setStatus(RequestStatus.values()[random.nextInt(RequestStatus.values().length)]);
        leave.setReason("Sample leave request");
        return leave;
    }

    private static void updateProjectManagers(ProjectRepository projectRepository, List<Project> projects, List<Employee> employees) {
        // Assign random managers from employees to projects
        projects.forEach(project -> {
            project.setManager(employees.get(random.nextInt(employees.size())));
        });
        projectRepository.saveAll(projects);
    }
}
