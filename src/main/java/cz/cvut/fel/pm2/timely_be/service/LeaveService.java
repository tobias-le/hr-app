package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.LeaveRequestDto;
import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.EmployeeLeaveBalance;
import cz.cvut.fel.pm2.timely_be.model.Leave;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeLeaveBalanceRepository;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public LeaveService(LeaveRepository leaveRepository, EmployeeLeaveBalanceRepository leaveBalanceRepository, EmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<LeaveRequestDto> getPendingRequests() {
        return leaveRepository.findByPendingStatus(RequestStatus.PENDING).stream().map(leave ->
            MapperUtils.leaveRequestDto(leave, employeeRepository.findById(leave.getEmployeeId()).get())).collect(Collectors.toList());
    }

    public Leave getLeaveRequestById(Long id) {
        return leaveRepository.findByLeaveId(id);
    }

    @Transactional
    public Leave approveLeaveRequest(Long id) {
        Leave leave = leaveRepository.findByLeaveId(id);
        leave.setStatus(RequestStatus.APPROVED);
        EmployeeLeaveBalance balance  = leaveBalanceRepository.findLeaveBalanceByEmployeeId(leave.getEmployeeId());
        switch (leave.getLeaveType()) {
            case SICK_LEAVE -> balance.setSickDaysLeft(balance.getSickDaysLeft() - leave.getLeaveAmount());
            case PERSONAL_LEAVE -> balance.setPersonalDaysLeft(balance.getPersonalDaysLeft() - leave.getLeaveAmount());
            case VACATION_LEAVE -> balance.setVacationDaysLeft(balance.getVacationDaysLeft() - leave.getLeaveAmount());
        }
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

    public List<Leave> getLeaveRequestsByEmployeeId(Long employeeId) {
        return leaveRepository.findLeaveRequestsByEmployeeId(employeeId);
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
