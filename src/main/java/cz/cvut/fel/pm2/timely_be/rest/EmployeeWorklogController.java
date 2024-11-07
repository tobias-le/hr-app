package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.AttendanceRecordDto;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.service.AttendanceService;
import cz.cvut.fel.pm2.timely_be.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees/{employeeId}/worklog")
@Tag(name = "Employee Worklog", description = "The Employee Worklog API")
public class EmployeeWorklogController {

    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;

    @Autowired
    public EmployeeWorklogController(EmployeeService employeeService, AttendanceService attendanceService) {
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
    }

    @GetMapping
    @Operation(summary = "Get worklog for employee", description = "Returns the last 5 worklog records for a given employee")
    public ResponseEntity<List<AttendanceRecordDto>> getWorklogForEmployee(@PathVariable Long employeeId) {
        try {
            Employee employee = employeeService.getEmployee(employeeId);
            List<AttendanceRecordDto> worklog = attendanceService.getAttendanceRecordsByMember(employee)
                    .stream()
                    .sorted(Comparator.comparing(AttendanceRecordDto::getDate, Comparator.reverseOrder()))
                    .limit(5)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(worklog);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }
}