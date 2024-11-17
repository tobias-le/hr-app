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
        // Find C-level executives
        Employee ceo = findEmployeeByTitle(employees, "Chief Executive Officer");
        Employee cto = findEmployeeByTitle(employees, "Chief Technology Officer");
        Employee cfo = findEmployeeByTitle(employees, "Chief Financial Officer");
        Employee coo = findEmployeeByTitle(employees, "Chief Operations Officer");

        // Create company-wide team under CEO
        Team companyTeam = createTeam("Company Leadership", ceo);
        companyTeam = teamRepository.save(companyTeam);

        // Create main divisions under CEO
        Team technologyDivision = createTeam("Technology Division", cto);
        Team financeDivision = createTeam("Finance Division", cfo);
        Team operationsDivision = createTeam("Operations Division", coo);

        // Set parent relationships for divisions
        technologyDivision.setParentTeam(companyTeam);
        financeDivision.setParentTeam(companyTeam);
        operationsDivision.setParentTeam(companyTeam);

        // Save divisions
        List<Team> divisions = teamRepository.saveAll(Arrays.asList(
            technologyDivision, financeDivision, operationsDivision
        ));

        // Create departments under Technology Division
        Employee infraManager = findEmployeeByTitle(employees, "Infrastructure Manager");
        Employee devDirector = findEmployeeByTitle(employees, "Development Director");
        
        Team infrastructureDept = createTeam("Infrastructure Department", infraManager);
        Team developmentDept = createTeam("Development Department", devDirector);
        
        infrastructureDept.setParentTeam(technologyDivision);
        developmentDept.setParentTeam(technologyDivision);

        // Create departments under Finance Division
        Employee financeDirector = findEmployeeByTitle(employees, "Finance Director");
        Team accountingDept = createTeam("Accounting Department", financeDirector);
        accountingDept.setParentTeam(financeDivision);

        // Create departments under Operations Division
        Employee hrDirector = findEmployeeByTitle(employees, "HR Director");
        Team hrDept = createTeam("HR Department", hrDirector);
        hrDept.setParentTeam(operationsDivision);

        // Save all departments
        List<Team> departments = teamRepository.saveAll(Arrays.asList(
            infrastructureDept, developmentDept, accountingDept, hrDept
        ));

        // Create teams under Infrastructure Department
        Team networkTeam = createTeam("Network Team", findEmployeeByTitle(employees, "Senior Network Engineer"));
        Team cloudTeam = createTeam("Cloud Team", findEmployeeByTitle(employees, "Cloud Architect"));
        Team securityTeam = createTeam("Security Team", findEmployeeByTitle(employees, "Security Specialist"));
        
        networkTeam.setParentTeam(infrastructureDept);
        cloudTeam.setParentTeam(infrastructureDept);
        securityTeam.setParentTeam(infrastructureDept);

        // Create teams under Development Department
        Team frontendTeam = createTeam("Frontend Team", findEmployeeByTitle(employees, "Frontend Developer"));
        Team backendTeam = createTeam("Backend Team", findEmployeeByTitle(employees, "Backend Developer"));
        Team qaTeam = createTeam("QA Team", findEmployeeByTitle(employees, "QA Engineer"));
        
        frontendTeam.setParentTeam(developmentDept);
        backendTeam.setParentTeam(developmentDept);
        qaTeam.setParentTeam(developmentDept);

        // Save all teams
        List<Team> teams = teamRepository.saveAll(Arrays.asList(
            networkTeam, cloudTeam, securityTeam,
            frontendTeam, backendTeam, qaTeam
        ));

        // Assign employees to their respective teams
        assignEmployeesToTeams(employees, companyTeam, divisions, departments, teams);

        // Save all teams again with updated members
        List<Team> allTeams = new ArrayList<>();
        allTeams.add(companyTeam);
        allTeams.addAll(divisions);
        allTeams.addAll(departments);
        allTeams.addAll(teams);
        return teamRepository.saveAll(allTeams);
    }

    private static void assignEmployeesToTeams(List<Employee> employees, Team companyTeam, 
            List<Team> divisions, List<Team> departments, List<Team> teams) {
        for (Employee employee : employees) {
            String title = employee.getJobTitle().toLowerCase();
            
            // Assign C-level to company team
            if (title.contains("chief")) {
                companyTeam.addMember(employee);
                employee.setTeam(companyTeam);
            }
            // Assign directors to respective divisions
            else if (title.contains("director")) {
                findTeamByName(divisions, getDivisionForDirector(title))
                    .ifPresent(team -> {
                        team.addMember(employee);
                        employee.setTeam(team);
                    });
            }
            // Assign managers to departments
            else if (title.contains("manager")) {
                findTeamByName(departments, getDepartmentForManager(title))
                    .ifPresent(team -> {
                        team.addMember(employee);
                        employee.setTeam(team);
                    });
            }
            // Assign other employees to specific teams
            else {
                findTeamByName(teams, getTeamForEmployee(title))
                    .ifPresent(team -> {
                        team.addMember(employee);
                        employee.setTeam(team);
                    });
            }
        }
    }

    private static Optional<Team> findTeamByName(List<Team> teams, String name) {
        return teams.stream()
            .filter(t -> t.getName().toLowerCase().contains(name.toLowerCase()))
            .findFirst();
    }

    private static String getDivisionForDirector(String title) {
        if (title.contains("technology")) return "Technology Division";
        if (title.contains("finance")) return "Finance Division";
        if (title.contains("operations")) return "Operations Division";
        return "";
    }

    private static String getDepartmentForManager(String title) {
        if (title.contains("infrastructure")) return "Infrastructure Department";
        if (title.contains("development")) return "Development Department";
        if (title.contains("accounting")) return "Accounting Department";
        if (title.contains("hr")) return "HR Department";
        return "";
    }

    private static String getTeamForEmployee(String title) {
        if (title.contains("network")) return "Network Team";
        if (title.contains("cloud")) return "Cloud Team";
        if (title.contains("security")) return "Security Team";
        if (title.contains("frontend")) return "Frontend Team";
        if (title.contains("backend")) return "Backend Team";
        if (title.contains("qa")) return "QA Team";
        return "";
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
        // Assign random managers from employees to projects
        projects.forEach(project -> {
            project.setManager(employees.get(random.nextInt(employees.size())));
        });
        projectRepository.saveAll(projects);
    }
}
