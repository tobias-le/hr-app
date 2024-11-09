package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.LeaveDto;
import cz.cvut.fel.pm2.timely_be.enums.LeaveStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.EmployeeLeaveBalance;
import cz.cvut.fel.pm2.timely_be.model.Leave;
import cz.cvut.fel.pm2.timely_be.service.EmployeeService;
import cz.cvut.fel.pm2.timely_be.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/leave")
@Tag(name = "Leave Requests", description = "Leave Requests API")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "Create a new leave request", description = "Creates a new leave request for an employee with the specified details.")
    public ResponseEntity<?> createLeaveRequest(@RequestBody @Valid LeaveDto leaveDto) {
        try {
            Leave leave = new Leave();
            leave.setEmployeeId(leaveDto.getEmployeeId());
            leave.setLeaveType(leaveDto.getLeaveType());
            leave.setStartDate(leaveDto.getStartDate());
            leave.setEndDate(leaveDto.getEndDate());
            leave.setStatus(LeaveStatus.PENDING);
            leave.setLeaveAmount(leaveDto.getLeaveAmount());
            leave.setReason(leaveDto.getReason());              //nesetoval se field a vyhazvala se vyjimka
            Leave createdLeave = leaveService.createLeaveRequest(leave);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLeave);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating leave request");
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all leave requests", description = "Retrieves a list of all leave requests in the system.")
    public ResponseEntity<?> getAllLeaveRequests() {
        try {
            List<Leave> leaveRequests = leaveService.getAllLeaveRequests();
            return leaveRequests.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(leaveRequests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving leave requests");
        }
    }

    @GetMapping("/{employeeId}")
    @Operation(summary = "Get leave requests by employee ID", description = "Retrieves a list of leave requests for a specific employee by their ID.")
    public ResponseEntity<?> getLeaveRequestsByEmployeeId(@PathVariable Long employeeId) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            List<Leave> leaveRequests = leaveService.getLeaveRequestsByEmployeeId(employeeId);
            return employee.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee ID not found") : ResponseEntity.ok(leaveRequests);      //co když pouze employee nemá requesty??
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving leave requests by employee ID");
        }
    }

    @GetMapping("/{employeeId}/reason")
    @Operation(summary = "Get reason for leave request", description = "Retrieves the reason for a specific leave request by its ID.")
    public ResponseEntity<?> getLeaveReasonById(@PathVariable Long employeeId) {
        try {
            String reason = leaveService.getReasonById(employeeId);
            return Optional.ofNullable(reason).map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave request ID not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving reason for leave request");
        }
    }

    @GetMapping("/{employeeId}/pending")
    @Operation(summary = "Get pending leave requests by employee ID", description = "Retrieves a list of pending leave requests for a specific employee by their ID.")
    public ResponseEntity<?> getPendingLeaveRequestsByEmployeeId(@PathVariable Long employeeId) {
        try {
            List<Leave> pendingRequests = leaveService.getPendingLeaveRequestsByEmployeeId(employeeId);
            return pendingRequests.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No pending requests found for the employee") : ResponseEntity.ok(pendingRequests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving pending leave requests by employee ID");
        }
    }

    @GetMapping("/{employeeId}/balance")
    @Operation(summary = "Get leave balance by employee ID", description = "Retrieves the leave balance for a specific employee by their ID.")
    public ResponseEntity<?> getLeaveBalance(@PathVariable Long employeeId) {
        try {
            EmployeeLeaveBalance balance = leaveService.getLeaveBalanceByEmployeeId(employeeId);
            return balance != null ? ResponseEntity.ok(balance) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee ID not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving leave balance");
        }
    }

    @GetMapping("/balances/personal-days")
    @Operation(summary = "Get employees with personal days left", description = "Retrieves a list of employees who have remaining personal days.")
    public ResponseEntity<?> getEmployeesWithPersonalDaysLeft() {
        try {
            List<EmployeeLeaveBalance> employeesWithPersonalDays = leaveService.getEmployeesWithPersonalDaysLeft();
            return employeesWithPersonalDays.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(employeesWithPersonalDays);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving employees with personal days left");
        }
    }

    @GetMapping("/balances/sick-days")
    @Operation(summary = "Get employees with sick days left", description = "Retrieves a list of employees who have remaining sick days.")
    public ResponseEntity<?> getEmployeesWithSickDaysLeft() {
        try {
            List<EmployeeLeaveBalance> employeesWithSickDays = leaveService.getEmployeesWithSickDaysLeft();
            return employeesWithSickDays.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(employeesWithSickDays);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving employees with sick days left");
        }
    }

    @GetMapping("/balances/vacation-days")
    @Operation(summary = "Get employees with vacation days left", description = "Retrieves a list of employees who have remaining vacation days.")
    public ResponseEntity<?> getEmployeesWithVacationDaysLeft() {
        try {
            List<EmployeeLeaveBalance> employeesWithVacationDays = leaveService.getEmployeesWithVacationDaysLeft();
            return employeesWithVacationDays.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(employeesWithVacationDays);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving employees with vacation days left");
        }
    }

    @GetMapping("/types/{leaveType}")
    @Operation(summary = "Get leave requests by type", description = "Retrieves a list of leave requests filtered by a specific leave type.")
    public ResponseEntity<?> getLeaveRequestsByType(@PathVariable LeaveType leaveType) {
        try {
            List<Leave> leaveRequests = leaveService.getLeaveRequestsByType(leaveType);
            return leaveRequests.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No leave requests found for specified type") : ResponseEntity.ok(leaveRequests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving leave requests by type");
        }
    }

    @GetMapping("/statuses/{status}")
    @Operation(summary = "Get leave requests by status", description = "Retrieves a list of leave requests filtered by a specific leave status.")
    public ResponseEntity<?> getLeaveRequestsByStatus(@PathVariable LeaveStatus status) {
        try {
            List<Leave> leaveRequests = leaveService.getLeaveRequestsByStatus(status);
            return leaveRequests.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No leave requests found for specified status") : ResponseEntity.ok(leaveRequests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving leave requests by status");
        }
    }
}
