package cz.cvut.fel.pm2.hrapp.employeemanagement.service;

import cz.cvut.fel.pm2.hrapp.employeemanagement.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cz.cvut.fel.pm2.hrapp.employeemanagement.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees() {
        var temp = new Employee();
        temp.setName("John Doe");
        return List.of( temp);
        //return employeeRepository.findAll();
    }

    public Long saveEmployee(Employee employee) {
        return employeeRepository.save(employee).getEmployeeId();
    }

    public Employee getEmployee(Long employeeId) {
        var employee = employeeRepository.findById(employeeId);
        if (employee.isEmpty()) {
            throw new IllegalArgumentException("Employee with id " + employeeId + " not found");
        }
        return employee.get();
    }

}
