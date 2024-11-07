package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employees/{employeeId}/projects")
@Tag(name = "Employee Projects", description = "The Employee Projects API")
public class EmployeeProjectController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeProjectController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @Operation(summary = "Get projects for employee", description = "Returns a list of projects for a given employee")
    public ResponseEntity<List<Project>> getProjectsForEmployee(@PathVariable Long employeeId) {
        try {
            Employee employee = employeeService.getEmployee(employeeId);
            List<Project> projects = employee.getCurrentProjects();
            return ResponseEntity.ok(projects);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); //return 404 Not Found
        }
    }
}