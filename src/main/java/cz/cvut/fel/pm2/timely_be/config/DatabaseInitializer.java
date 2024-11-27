package cz.cvut.fel.pm2.timely_be.config;

import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import cz.cvut.fel.pm2.timely_be.model.*;
import cz.cvut.fel.pm2.timely_be.enums.EmploymentType;
import cz.cvut.fel.pm2.timely_be.repository.*;
import cz.cvut.fel.pm2.timely_be.repository.composite.EmployeeLearningId;
import cz.cvut.fel.pm2.timely_be.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.FULL_TIME;
import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.PART_TIME;

@Configuration
@Slf4j
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initEmployeeDatabase(EmployeeRepository employeeRepository, TeamRepository teamRepository
            , AttendanceRecordRepository attendanceRecordRepository, ProjectRepository projectRepository, LeaveRepository leaveRepository, EmployeeLeaveBalanceRepository leaveBalanceRepository, UserService userService, LearningRepository learningRepository, EmployeeLearningRepository employeeLearningRepository) {
        return args -> resetDatabaseToBaseState(employeeRepository, teamRepository, attendanceRecordRepository, projectRepository, leaveRepository, leaveBalanceRepository, userService, learningRepository, employeeLearningRepository);
    }

    public static void resetDatabaseToBaseState(EmployeeRepository employeeRepository, TeamRepository teamRepository,
            AttendanceRecordRepository attendanceRecordRepository, ProjectRepository projectRepository,
            LeaveRepository leaveRepository, EmployeeLeaveBalanceRepository leaveBalanceRepository, UserService userService,
                                                LearningRepository learningRepository,
                                                EmployeeLearningRepository employeeLearningRepository) {
        
        // Initialize base data in correct order
        var projects = initializeProjects(projectRepository);
        var employees = initializeEmployees(employeeRepository, Collections.emptyList());
        
        // Create users for all employees
        createUsersForEmployees(employees, userService);
        
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
        initializeTeams(teamRepository, employees);
        
        // Save employees with updated projects and team assignments
        employeeRepository.saveAll(employees);
        
        // Initialize attendance records
        initializeAttendanceRecords(attendanceRecordRepository, employees);
        
        // Initialize leave balances and requests
        initializeLeaveData(leaveRepository, leaveBalanceRepository, employees);
        
        // Initialize learnings
        initializeLearnings(learningRepository, employeeLearningRepository, employees);
    }

    private static List<Project> initializeProjects(ProjectRepository projectRepository) {
        // Create exactly 6 projects
        var projects = Arrays.asList(
            createProject("Cloud Infrastructure Migration"),
            createProject("Mobile App Development"),
            createProject("Web Platform Redesign"),
            createProject("Security Systems Overhaul"),
            createProject("Analytics Platform Development"),
            createProject("ERP System Implementation")
        );
        return projectRepository.saveAll(projects);
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

    private static void initializeTeams(TeamRepository teamRepository, List<Employee> employees) {
        // Create main departments under CTO
        Team technologyDepartment = createTeam("Technology Department", findEmployeeByTitle(employees, "Chief Technology Officer"));
        Team hrDepartment = createTeam("HR Department", findEmployeeByTitle(employees, "HR Director"));
        Team financeDepartment = createTeam("Finance Department", findEmployeeByTitle(employees, "Chief Financial Officer"));
        
        // Create sub-teams under Technology Department
        Team developmentTeam = createTeam("Development Team", findEmployeeByTitle(employees, "Development Director"));
        developmentTeam.setParentTeam(technologyDepartment);
        
        Team infrastructureTeam = createTeam("Infrastructure Team", findEmployeeByTitle(employees, "Infrastructure Manager"));
        infrastructureTeam.setParentTeam(technologyDepartment);
        
        Team securityTeam = createTeam("Security Team", findEmployeeByTitle(employees, "Security Specialist"));
        securityTeam.setParentTeam(infrastructureTeam);  // Security reports to Infrastructure
        
        // Use ArrayList to make the list mutable
        List<Team> teams = new ArrayList<>(Arrays.asList(
            technologyDepartment, hrDepartment, financeDepartment,
            developmentTeam, infrastructureTeam, securityTeam
        ));

        // Assign employees to teams based on their job titles
        for (Employee employee : employees) {
            String title = employee.getJobTitle().toLowerCase();
            String name = employee.getName().toLowerCase();
            
            // C-Suite assignments
            if (title.contains("chief technology officer")) {
                assignEmployeeToTeam(employee, technologyDepartment);
            } else if (title.contains("chief financial officer")) {
                assignEmployeeToTeam(employee, financeDepartment);
            }
            // Department-level assignments
            else if (title.contains("hr")) {
                assignEmployeeToTeam(employee, hrDepartment);
            } else if (title.contains("finance") || title.contains("accountant") || title.contains("payroll")) {
                assignEmployeeToTeam(employee, financeDepartment);
            }
            // Technology sub-team assignments
            else if (title.contains("developer") || title.contains("frontend") || title.contains("backend") || 
                     title.contains("qa engineer") || title.contains("development director")) {
                assignEmployeeToTeam(employee, developmentTeam);
            } else if (title.contains("infrastructure") || title.contains("network") || 
                       title.contains("cloud architect") || title.contains("system admin")) {
                assignEmployeeToTeam(employee, infrastructureTeam);
            } else if (title.contains("security")) {
                assignEmployeeToTeam(employee, securityTeam);
            }
            // CEO assignment
            else if (name.contains("john smith") || title.contains("chief executive officer")) {
                assignEmployeeToTeam(employee, technologyDepartment);  // CEO joins Technology Department
            }
        }

        teamRepository.saveAll(teams);
    }

    private static void assignEmployeeToTeam(Employee employee, Team team) {
        team.addMember(employee);
        employee.setTeam(team);
    }

    private static Employee findEmployeeByTitle(List<Employee> employees, String title) {
        return employees.stream()
                .filter(e -> e.getJobTitle().equals(title))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee with title " + title + " not found"));
    }

    private static void initializeAttendanceRecords(AttendanceRecordRepository attendanceRecordRepository,
                                                  List<Employee> employees) {
        // Get next Monday's date
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        
        // Create records in batches to reduce memory usage
        int batchSize = 100;
        List<AttendanceRecord> recordsBatch = new ArrayList<>(batchSize);
        
        for (Employee employee : employees) {
            if (employee.getCurrentProjects().isEmpty()) continue;
            
            List<Project> employeeProjects = employee.getCurrentProjects();
            for (int i = 0; i < 5; i++) { // Monday to Friday
                LocalDate date = nextMonday.plusDays(i);
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
                
                recordsBatch.add(record);
                
                // Save batch when it reaches the size limit
                if (recordsBatch.size() >= batchSize) {
                    attendanceRecordRepository.saveAll(recordsBatch);
                    recordsBatch.clear();
                }
            }
        }
        
        // Save any remaining records
        if (!recordsBatch.isEmpty()) {
            attendanceRecordRepository.saveAll(recordsBatch);
        }
    }

    private static void initializeLeaveData(LeaveRepository leaveRepository, 
            EmployeeLeaveBalanceRepository leaveBalanceRepository, List<Employee> employees) {
        // Initialize leave balances for all employees
        List<EmployeeLeaveBalance> balances = employees.stream()
            .map(employee -> createLeaveBalance(employee.getEmployeeId()))
            .collect(Collectors.toList());
        
        // Initialize leave requests
        List<Leave> leaveRequests = employees.stream()
            .filter(e -> random.nextBoolean()) // 50% chance for each employee
            .map(employee -> createLeaveRequest(employee.getEmployeeId()))
            .collect(Collectors.toList());

        // Batch save
        leaveBalanceRepository.saveAll(balances);
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
        employee.setPhoneNumber(generateDeterministicPhone(name));
        
        // Set HR flag based on job title (case-insensitive)
        if (jobTitle.toLowerCase().contains("hr ") || 
            jobTitle.toLowerCase().startsWith("hr") || 
            jobTitle.toLowerCase().endsWith("hr")) {
            employee.setHR(true);
        }
        
        // Set new fields based on job title
        if (jobTitle.toLowerCase().contains("chief")) {
            employee.setAnnualSalary(200000);
            employee.setAnnualLearningBudget(20000);
            employee.setAnnualBusinessPerformanceBonusMax(100000);
            employee.setAnnualPersonalPerformanceBonusMax(100000);
        } else if (jobTitle.toLowerCase().contains("director")) {
            employee.setAnnualSalary(150000);
            employee.setAnnualLearningBudget(15000);
            employee.setAnnualBusinessPerformanceBonusMax(50000);
            employee.setAnnualPersonalPerformanceBonusMax(50000);
        } else if (jobTitle.toLowerCase().contains("manager")) {
            employee.setAnnualSalary(120000);
            employee.setAnnualLearningBudget(10000);
            employee.setAnnualBusinessPerformanceBonusMax(30000);
            employee.setAnnualPersonalPerformanceBonusMax(30000);
        } else if (jobTitle.toLowerCase().contains("senior")) {
            employee.setAnnualSalary(100000);
            employee.setAnnualLearningBudget(8000);
            employee.setAnnualBusinessPerformanceBonusMax(20000);
            employee.setAnnualPersonalPerformanceBonusMax(20000);
        } else {
            employee.setAnnualSalary(80000);
            employee.setAnnualLearningBudget(5000);
            employee.setAnnualBusinessPerformanceBonusMax(10000);
            employee.setAnnualPersonalPerformanceBonusMax(10000);
        }

        // Assign up to 3 projects deterministically
        if (!projects.isEmpty()) {
            int projectCount = Math.min(3, projects.size());
            employee.setCurrentProjects(new ArrayList<>(projects.subList(0, projectCount)));
        }
        
        return employee;
    }

    private static Team createTeam(String name, Employee manager) {
        Team team = new Team();
        team.setName(name);
        team.setManager(manager);
        team.setMembers(new HashSet<>());
        return team;
    }

    private static final Random random = new Random();

    private static String generateDeterministicPhone(String name) {
        // Generate a deterministic 9-digit phone number based on name hash
        return String.format("%09d", Math.abs(name.hashCode() % 1000000000));
    }

    private static RequestStatus getRandomStatus() {
        return RequestStatus.values()[random.nextInt(RequestStatus.values().length)];
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
        leave.setDatetime(LocalDateTime.now());
        leave.setLeaveAmount(duration);
        leave.setStatus(RequestStatus.values()[random.nextInt(RequestStatus.values().length)]);
        leave.setReason("Sample leave request");
        return leave;
    }

    private static void updateProjectManagers(ProjectRepository projectRepository, List<Project> projects, List<Employee> employees) {
        projects.forEach(project -> project.setManager(employees.get(random.nextInt(employees.size()))));
        projectRepository.saveAll(projects);
    }

    private static void createUsersForEmployees(List<Employee> employees, UserService userService) {
        List<User> users = employees.stream()
            .map(employee -> {
                User user = new User();
                user.setEmail(employee.getEmail());
                user.setPassword("pass123");
                user.setEmployee(employee);
                return user;
            })
            .collect(Collectors.toList());

        try {
            userService.createUsers(users); // Batch insert users
        } catch (Exception e) {
            log.error("Failed to create users batch", e);
        }
    }

    private static void initializeLearnings(LearningRepository learningRepository,
                                              EmployeeLearningRepository employeeLearningRepository,
                                              List<Employee> employees) {
        // Create common learnings
        List<Learning> commonLearnings = Arrays.asList(
            createLearning("Cybersecurity Fundamentals", "https://learning.company.com/security-basics"),
            createLearning("Business Communication", "https://learning.company.com/business-comm"),
            createLearning("Project Management Essentials", "https://learning.company.com/pm-basics")
        );
        
        // Create developer-specific learning
        Learning developerLearning = createLearning("Advanced Microservices Architecture", 
                "https://learning.company.com/microservices");
        
        // Save all learnings
        learningRepository.saveAll(commonLearnings);
        learningRepository.save(developerLearning);
        
        // Assign common learnings to all employees
        List<EmployeeLearning> employeeLearnings = new ArrayList<>();
        for (Employee employee : employees) {
            for (Learning learning : commonLearnings) {
                employeeLearnings.add(createEmployeeLearning(employee, learning));
            }
            
            // Assign developer learning only to developers
            if (employee.getJobTitle().toLowerCase().contains("developer")) {
                employeeLearnings.add(createEmployeeLearning(employee, developerLearning));
            }
        }
        
        employeeLearningRepository.saveAll(employeeLearnings);
    }

    private static Learning createLearning(String name, String link) {
        Learning learning = new Learning();
        learning.setName(name);
        learning.setLink(link);
        return learning;
    }

    private static EmployeeLearning createEmployeeLearning(Employee employee, Learning learning) {
        EmployeeLearning employeeLearning = new EmployeeLearning();
        EmployeeLearningId id = new EmployeeLearningId();
        id.setEmployeeId(employee.getEmployeeId());
        id.setLearningId(learning.getLearningId());
        employeeLearning.setId(id);
        employeeLearning.setEmployee(employee);
        employeeLearning.setLearning(learning);
        employeeLearning.setDate(LocalDate.now().minusDays(random.nextInt(30))); // Random date within last 30 days
        return employeeLearning;
    }
}
