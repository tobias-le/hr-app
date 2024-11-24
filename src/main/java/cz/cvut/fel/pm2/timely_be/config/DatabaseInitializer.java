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
import java.util.*;
import java.util.stream.Collectors;

import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.FULL_TIME;

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
        
        // First create and save all projects
        var projects = initializeProjects(projectRepository);
        projects = projectRepository.saveAll(projects);  // Save projects first and get managed entities
        
        // Then create and save employees with the saved projects
        var employees = initializeEmployees(employeeRepository, projects);
        
        // Update projects with managers and save again
        updateProjectManagers(projectRepository, projects, employees);
        
        // Initialize teams and assign employees to teams
        var teams = initializeTeams(teamRepository, employees);
        
        // Save employees with updated team assignments
        employeeRepository.saveAll(employees);
        
        // Initialize attendance records
        initializeAttendanceRecords(attendanceRecordRepository, employees, projects);
        
        // Initialize leave balances and requests
        initializeLeaveData(leaveRepository, leaveBalanceRepository, employees);
    }

    private static List<Project> initializeProjects(ProjectRepository projectRepository) {
        // Create exactly 5 projects
        var projectA = createProject("Digital Transformation");
        var projectB = createProject("Cloud Migration");
        var projectC = createProject("Mobile App Development");
        var projectD = createProject("Security Systems");
        var projectE = createProject("Analytics Platform");

        return Arrays.asList(projectA, projectB, projectC, projectD, projectE);
    }

    private static List<Employee> initializeEmployees(EmployeeRepository employeeRepository, List<Project> projects) {
        List<Employee> allEmployees = new ArrayList<>();
        
        // Assign fixed projects to employees instead of random
        // C-Suite (2 projects each)
        allEmployees.addAll(Arrays.asList(
            createEmployee("John Smith", "Chief Executive Officer", FULL_TIME, "john.smith@company.com", 
                projects.subList(0, 2)),
            createEmployee("Sarah Johnson", "Chief Technology Officer", FULL_TIME, "sarah.johnson@company.com", 
                projects.subList(1, 3))
        ));

        // Department Heads (3 projects each)
        allEmployees.addAll(Arrays.asList(
            createEmployee("David Wilson", "Infrastructure Manager", FULL_TIME, "david.wilson@company.com", 
                Arrays.asList(projects.get(1), projects.get(3), projects.get(4))),
            createEmployee("Patricia White", "Development Director", FULL_TIME, "patricia.white@company.com", 
                Arrays.asList(projects.get(0), projects.get(2), projects.get(4)))
        ));

        // Team Members (2 projects each)
        allEmployees.addAll(Arrays.asList(
            createEmployee("Lisa Brown", "Senior Network Engineer", FULL_TIME, "lisa.brown@company.com", 
                Arrays.asList(projects.get(1), projects.get(3))),
            createEmployee("James Taylor", "Cloud Architect", FULL_TIME, "james.taylor@company.com", 
                Arrays.asList(projects.get(1), projects.get(4))),
            createEmployee("Thomas Anderson", "Senior Software Engineer", FULL_TIME, "thomas.anderson@company.com", 
                Arrays.asList(projects.get(0), projects.get(2))),
            createEmployee("Maria Garcia", "Frontend Developer", FULL_TIME, "maria.garcia@company.com", 
                Arrays.asList(projects.get(2), projects.get(4)))
        ));

        return employeeRepository.saveAll(allEmployees);
    }

    private static List<Team> initializeTeams(TeamRepository teamRepository, List<Employee> employees) {
        // Find key employees
        Employee ceo = findEmployeeByTitle(employees, "Chief Executive Officer");
        Employee cto = findEmployeeByTitle(employees, "Chief Technology Officer");
        Employee infraManager = findEmployeeByTitle(employees, "Infrastructure Manager");
        Employee devDirector = findEmployeeByTitle(employees, "Development Director");

        // Create 6 teams with clear hierarchy
        // Level 1
        Team companyTeam = createTeam("Company Leadership", ceo);
        companyTeam = teamRepository.save(companyTeam);

        // Level 2
        Team technologyDivision = createTeam("Technology Division", cto);
        technologyDivision.setParentTeam(companyTeam);
        technologyDivision = teamRepository.save(technologyDivision);

        // Level 3
        Team infrastructureDept = createTeam("Infrastructure Department", infraManager);
        Team developmentDept = createTeam("Development Department", devDirector);
        infrastructureDept.setParentTeam(technologyDivision);
        developmentDept.setParentTeam(technologyDivision);

        // Level 4
        Team networkTeam = createTeam("Network Team", findEmployeeByTitle(employees, "Senior Network Engineer"));
        Team developmentTeam = createTeam("Development Team", findEmployeeByTitle(employees, "Senior Software Engineer"));
        networkTeam.setParentTeam(infrastructureDept);
        developmentTeam.setParentTeam(developmentDept);

        // Save all teams
        List<Team> allTeams = Arrays.asList(
            companyTeam, technologyDivision, infrastructureDept, 
            developmentDept, networkTeam, developmentTeam
        );

        // Assign employees to teams deterministically
        assignEmployeesToTeams(employees, allTeams);

        return teamRepository.saveAll(allTeams);
    }

    private static void assignEmployeesToTeams(List<Employee> employees, List<Team> allTeams) {
        Map<String, Team> teamsByName = allTeams.stream()
            .collect(Collectors.toMap(Team::getName, team -> team));

        for (Employee employee : employees) {
            Team team = switch (employee.getJobTitle()) {
                case "Chief Executive Officer", "Chief Technology Officer" -> 
                    teamsByName.get("Company Leadership");
                case "Infrastructure Manager" -> 
                    teamsByName.get("Infrastructure Department");
                case "Development Director" -> 
                    teamsByName.get("Development Department");
                case "Senior Network Engineer", "Cloud Architect" -> 
                    teamsByName.get("Network Team");
                case "Senior Software Engineer", "Frontend Developer" -> 
                    teamsByName.get("Development Team");
                default -> null;
            };
            
            if (team != null) {
                team.addMember(employee);
                employee.setTeam(team);
            }
        }
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
                // Use first project for even days, second project for odd days
                Project project = employeeProjects.get(i % employeeProjects.size());
                
                AttendanceRecord record = new AttendanceRecord();
                record.setMember(employee);
                record.setProject(project);
                record.setDate(date);
                record.setClockInTime(LocalDateTime.of(date, LocalTime.of(9, 0)));
                record.setClockOutTime(LocalDateTime.of(date, LocalTime.of(17, 0)));
                record.setDescription("Work on " + project.getName());
                
                switch (i % 3) {
                    case 0:
                        record.setStatus(RequestStatus.APPROVED);
                        break;
                    case 1:
                        record.setStatus(RequestStatus.PENDING);
                        break;
                    case 2:
                        record.setStatus(RequestStatus.REJECTED);
                        break;
                }
                
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
        
        // Initialize salary-related fields based on job title
        switch (jobTitle) {
            case "Chief Executive Officer":
                employee.setAnnualSalary(200000);
                employee.setAnnualLearningBudget(10000);
                employee.setAnnualBusinessPerformanceBonusMax(100000);
                employee.setAnnualPersonalPerformanceBonusMax(50000);
                break;
            case "Chief Technology Officer":
                employee.setAnnualSalary(180000);
                employee.setAnnualLearningBudget(8000);
                employee.setAnnualBusinessPerformanceBonusMax(90000);
                employee.setAnnualPersonalPerformanceBonusMax(45000);
                break;
            case "Infrastructure Manager":
            case "Development Director":
                employee.setAnnualSalary(150000);
                employee.setAnnualLearningBudget(6000);
                employee.setAnnualBusinessPerformanceBonusMax(45000);
                employee.setAnnualPersonalPerformanceBonusMax(30000);
                break;
            case "Senior Network Engineer":
            case "Cloud Architect":
            case "Senior Software Engineer":
                employee.setAnnualSalary(120000);
                employee.setAnnualLearningBudget(5000);
                employee.setAnnualBusinessPerformanceBonusMax(24000);
                employee.setAnnualPersonalPerformanceBonusMax(18000);
                break;
            default: // Frontend Developer and other roles
                employee.setAnnualSalary(90000);
                employee.setAnnualLearningBudget(4000);
                employee.setAnnualBusinessPerformanceBonusMax(18000);
                employee.setAnnualPersonalPerformanceBonusMax(13500);
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
        // Assign specific managers to projects
        Map<String, Employee> employeesByTitle = employees.stream()
            .collect(Collectors.toMap(Employee::getJobTitle, e -> e));

        projects.get(0).setManager(employeesByTitle.get("Chief Executive Officer"));        // Digital Transformation
        projects.get(1).setManager(employeesByTitle.get("Chief Technology Officer"));       // Cloud Migration
        projects.get(2).setManager(employeesByTitle.get("Development Director"));           // Mobile App Development
        projects.get(3).setManager(employeesByTitle.get("Infrastructure Manager"));         // Security Systems
        projects.get(4).setManager(employeesByTitle.get("Senior Software Engineer"));       // Analytics Platform

        projectRepository.saveAll(projects);
    }

    private static Employee findEmployeeByTitle(List<Employee> employees, String title) {
        return employees.stream()
                .filter(e -> e.getJobTitle().equals(title))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee with title " + title + " not found"));
    }
}
