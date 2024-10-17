package cz.cvut.fel.pm2.hrapp.employeemanagement.rest;

import cz.cvut.fel.pm2.hrapp.employeemanagement.model.Employee;
import cz.cvut.fel.pm2.hrapp.employeemanagement.rest.dto.EmployeeDto;
import cz.cvut.fel.pm2.hrapp.employeemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee addEmployee(@RequestBody EmployeeDto employeeDto) {
        return employeeService.createEmployee(employeeDto);
    }
}
