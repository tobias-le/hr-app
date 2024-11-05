package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.enums.LeaveStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import cz.cvut.fel.pm2.timely_be.model.EmployeeLeaveBalance;
import cz.cvut.fel.pm2.timely_be.model.Leave;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeLeaveBalanceRepository;
import cz.cvut.fel.pm2.timely_be.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmployeeLeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    public LeaveService(LeaveRepository leaveRepository, EmployeeLeaveBalanceRepository leaveBalanceRepository) {
        this.leaveRepository = leaveRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    public EmployeeLeaveBalance getLeaveBalanceByEmployeeId(Long employeeId) {
        return leaveBalanceRepository.findLeaveBalanceByEmployeeId(employeeId);
    }

    public List<Leave> getLeaveRequestsByEmployeeId(Long employeeId) {
        return leaveRepository.findLeaveRequestsByEmployeeId(employeeId);
    }

    public Leave createLeaveRequest(Leave leave) {
        leave.setStatus(LeaveStatus.PENDING);
        return leaveRepository.save(leave);
    }

    public List<Leave> getAllLeaveRequests() {
        return leaveRepository.findAll();
    }

    public List<Leave> getPendingLeaveRequestsByEmployeeId(Long employeeId) {
        return leaveRepository.findPendingLeaveRequestsByEmployeeId(employeeId);
    }

    public List<Leave> getLeaveRequestsByType(LeaveType leaveType) {
        return leaveRepository.findByLeaveType(leaveType);
    }

    public List<Leave> getLeaveRequestsByStatus(LeaveStatus status) {
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
