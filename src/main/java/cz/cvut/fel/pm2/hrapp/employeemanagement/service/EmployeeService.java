package cz.cvut.fel.pm2.hrapp.employeemanagement.service;

import cz.cvut.fel.pm2.hrapp.employeemanagement.model.Employee;
import cz.cvut.fel.pm2.hrapp.employeemanagement.repository.DepartmentRepository;
import cz.cvut.fel.pm2.hrapp.employeemanagement.repository.EmployeeRepository;
import cz.cvut.fel.pm2.hrapp.employeemanagement.rest.dto.EmployeeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public Employee createEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setAddress(employeeDto.getAddress());
        employee.setContractType(employeeDto.getContractType());
        employee.setContractualHours(employeeDto.getContractualHours());
        employee.setWorkPercentage(employeeDto.getWorkPercentage());
        employee.setAccountNumber(employeeDto.getAccountNumber());
        employee.setAvailableTimeOff(employeeDto.getAvailableTimeOff());

        departmentRepository.findById(employeeDto.getDepartmentId())
                .ifPresent(employee::setDepartment);

        employeeRepository.findById(employeeDto.getSupervisorId())
                .ifPresent(employee::setSupervisor);

        return employeeRepository.save(employee);
    }
}
