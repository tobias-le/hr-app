package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.EmployeeNameWithIdDto;
import cz.cvut.fel.pm2.timely_be.dto.LeaveDto;
import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.EmployeeLeaveBalance;
import cz.cvut.fel.pm2.timely_be.model.Leave;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeLeaveBalanceRepository;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmployeeLeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    public LeaveService(LeaveRepository leaveRepository, EmployeeLeaveBalanceRepository leaveBalanceRepository) {
        this.leaveRepository = leaveRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    public List<Leave> getPendingRequests() {
        return leaveRepository.findByPendingStatus(RequestStatus.PENDING);
    }

    public Leave getLeaveRequestById(Long id) {
        return leaveRepository.findByLeaveId(id);
    }

    public Leave approveLeaveRequest(Long id) {
        Leave leave = leaveRepository.findByLeaveId(id);
        leave.setStatus(RequestStatus.APPROVED);
        return leaveRepository.save(leave);
    }

    public Leave rejectLeaveRequest(Long id) {
        Leave leave = leaveRepository.findByLeaveId(id);
        leave.setStatus(RequestStatus.REJECTED);
        return leaveRepository.save(leave);
    }

    public EmployeeLeaveBalance getLeaveBalanceByEmployeeId(Long employeeId) {
        return leaveBalanceRepository.findLeaveBalanceByEmployeeId(employeeId);
    }

    public List<LeaveDto> getLeaveRequestsByEmployeeId(Long employeeId) {
        List<Leave> leaves = leaveRepository.findLeaveRequestsByEmployeeId(employeeId);
        return leaves.stream()
                .map(leave -> {
                    LeaveDto leaveDto = new LeaveDto();
                    Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
                    leaveDto.setEmployee(new EmployeeNameWithIdDto(employee.getEmployeeId(), employee.getName()));
                    leaveDto.setLeaveType(leave.getLeaveType());
                    leaveDto.setStartDate(leave.getStartDate());
                    leaveDto.setEndDate(leave.getEndDate());
                    leaveDto.setLeaveStatus(leave.getStatus());
                    leaveDto.setLeaveAmount(leave.getLeaveAmount());
                    leaveDto.setReason(leave.getReason());
                    return leaveDto;
                })
                .collect(Collectors.toList());
    }

    public Leave createLeaveRequest(Leave leave) {
        leave.setStatus(RequestStatus.PENDING);
        return leaveRepository.save(leave);
    }

    public List<Leave> getAllLeaveRequests() {
        return leaveRepository.findAll();
    }

    public String getReasonById(Long id) {
        return leaveRepository.findReasonById(id);
    }

    public List<Leave> getPendingLeaveRequestsByEmployeeId(Long employeeId) {
        return leaveRepository.findPendingLeaveRequestsByEmployeeId(employeeId);
    }

    public List<Leave> getLeaveRequestsByType(LeaveType leaveType) {
        return leaveRepository.findByLeaveType(leaveType);
    }

    public List<Leave> getLeaveRequestsByStatus(RequestStatus status) {
        return leaveRepository.findByLeaveStatus(status);
    }

    public List<EmployeeLeaveBalance> getEmployeesWithPersonalDaysLeft() {
        return leaveBalanceRepository.findEmployeesWithPersonalDaysLeft();
    }

    public List<EmployeeLeaveBalance> getEmployeesWithSickDaysLeft() {
        return leaveBalanceRepository.findEmployeesWithSickDaysLeft();
    }

    public List<EmployeeLeaveBalance> getEmployeesWithVacationDaysLeft() {
        return leaveBalanceRepository.findEmployeesWithVacationDaysLeft();
    }
}
