package cz.cvut.fel.pm2.hrapp.employeemanagement.rest;

import cz.cvut.fel.pm2.hrapp.employeemanagement.dto.EmployeeDto;
import cz.cvut.fel.pm2.hrapp.employeemanagement.model.Employee;
import cz.cvut.fel.pm2.hrapp.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getEmployees() {
        return ResponseEntity.ok(employeeService.getEmployees().stream().map(Employee::toDto).toList());
    }

    @PostMapping
    public ResponseEntity<Void> saveEmployee(Employee employee) {
        return ResponseEntity.created(
                fromCurrentRequest().path("/{employeeId}")
                        .buildAndExpand(employeeService.saveEmployee(employee)).toUri()
        ).build();
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployee(Long employeeId) {
        return ResponseEntity.ok(employeeService.getEmployee(employeeId));
    }
}
