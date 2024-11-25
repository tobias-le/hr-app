package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.EmployeeDto;
import cz.cvut.fel.pm2.timely_be.dto.EmployeeNameWithIdDto;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;
import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.fromString;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> getEmployeesByTeam(Pageable pageable, long teamId) {
        return employeeRepository.findByTeamId(pageable, teamId);
    }

    public Page<Employee> getEmployeesByProject(Pageable pageable, long projectId) {
        return employeeRepository.findEmployeesByProjectIdPageable(projectId, pageable);
    }

    /**
     * this method only exists for the sake of testing, will be deleted later
     * @return list of all employees
     */
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Page<Employee> getAllEmployeesPage() {
        var pageable = PageRequest.of(0, (int) employeeRepository.count());
        return employeeRepository.findAll(pageable);
    }

    public Employee updateEmployee(Long id, EmployeeDto employeeDto) {
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setEmploymentType(fromString(employeeDto.getEmploymentStatus()));
        employee.setJobTitle(employeeDto.getJobTitle());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());

        employee.setAnnualSalary(employeeDto.getAnnualSalary());
        employee.setAnnualLearningBudget(employeeDto.getAnnualLearningBudget());
        employee.setAnnualBusinessPerformanceBonusMax(employeeDto.getAnnualBusinessPerformanceBonusMax());
        employee.setAnnualPersonalPerformanceBonusMax(employeeDto.getAnnualPersonalPerformanceBonusMax());


        return employeeRepository.save(employee);
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    public List<EmployeeNameWithIdDto> getAllEmployeeNamesWithIds() {
        return employeeRepository.findAllEmployeeNamesWithIds();
    }

    public List<EmployeeNameWithIdDto> autocompleteEmployees(String namePattern, List<Long> excludeIds) {
        if (namePattern == null || namePattern.trim().isEmpty()) {
            return Collections.emptyList();
        }
        if (excludeIds == null) {
            excludeIds = Collections.emptyList();
        }
        return employeeRepository.findEmployeesByNameContaining(namePattern.trim(), excludeIds, PageRequest.of(0, 5));
    }

    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }
}