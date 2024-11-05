package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.EmployeeDto;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static cz.cvut.fel.pm2.timely_be.enums.EmploymentStatus.fromString;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> getEmployees(Pageable pageable, long teamId) {
        return employeeRepository.findByTeamId(pageable, teamId);
    }

    public Employee updateEmployee(Long id, EmployeeDto employeeDto) {
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setEmploymentStatus(fromString(employeeDto.getEmploymentStatus()));
        employee.setJobTitle(employeeDto.getJobTitle());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());

        return employeeRepository.save(employee);
    }
}