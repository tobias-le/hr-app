package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.EmployeeDto;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee", description = "The Employee API")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @Operation(summary = "Get all employees in teams", description = "Get all employees in teams with pagination")
    public ResponseEntity<Page<EmployeeDto>> getEmployees(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam long teamId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeService.getEmployees(pageable, teamId);

        // Convert Employee entities to EmployeeDto using EmployeeMapper
        List<EmployeeDto> employeeDtoList = employeePage.getContent().stream()
                .map(MapperUtils::toDto)
                .collect(Collectors.toList());

        // Return a new Page<EmployeeDto> based on the mapped list and pageable information
        var result =  new PageImpl<>(employeeDtoList, pageable, employeePage.getTotalElements());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee", description = "Update employee by id")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDto);
        return ResponseEntity.ok(MapperUtils.toDto(updatedEmployee));
    }
}