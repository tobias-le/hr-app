package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.LeaveRequestDto;
import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.EmployeeLeaveBalance;
import cz.cvut.fel.pm2.timely_be.model.Leave;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeLeaveBalanceRepository;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.LeaveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LeaveServiceTest {
    @Mock
    private LeaveRepository leaveRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeLeaveBalanceRepository leaveBalanceRepository;

    @InjectMocks
    private LeaveService leaveService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetLeaveBalanceByEmployeeId() {
        Long employeeId = 1L;
        EmployeeLeaveBalance leaveBalance = new EmployeeLeaveBalance();
        leaveBalance.setEmployeeId(employeeId);

        when(leaveBalanceRepository.findLeaveBalanceByEmployeeId(employeeId)).thenReturn(leaveBalance);

        EmployeeLeaveBalance result = leaveService.getLeaveBalanceByEmployeeId(employeeId);
        assertNotNull(result);
        assertEquals(employeeId, result.getEmployeeId());
    }

    @Test
    public void testGetLeaveRequestsByEmployeeId() {
        Long employeeId = 1L;
        Leave leave = new Leave();
        leave.setEmployeeId(employeeId);

        when(leaveRepository.findLeaveRequestsByEmployeeId(employeeId)).thenReturn(List.of(leave));

        List<Leave> result = leaveService.getLeaveRequestsByEmployeeId(employeeId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(employeeId, result.get(0).getEmployeeId());
    }

    @Test
    public void testCreateLeaveRequest() {
        Leave leave = new Leave();
        leave.setLeaveAmount(8);
        leave.setStatus(RequestStatus.PENDING);

        when(leaveRepository.save(any(Leave.class))).thenReturn(leave);

        Leave result = leaveService.createLeaveRequest(leave);
        assertNotNull(result);
        assertEquals(RequestStatus.PENDING, result.getStatus());
    }

    @Test
    public void testGetAllLeaveRequests() {
        Leave leave = new Leave();
        when(leaveRepository.findAll()).thenReturn(List.of(leave));

        List<Leave> result = leaveService.getAllLeaveRequests();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetReasonById() {
        Long leaveId = 1L;
        String reason = "Vacation";

        when(leaveRepository.findReasonById(leaveId)).thenReturn(reason);

        String result = leaveService.getReasonById(leaveId);
        assertNotNull(result);
        assertEquals(reason, result);
    }

    @Test
    public void testGetPendingLeaveRequestsByEmployeeId() {
        Long employeeId = 1L;
        Leave leave = new Leave();
        leave.setStatus(RequestStatus.PENDING);

        when(leaveRepository.findPendingLeaveRequestsByEmployeeId(employeeId)).thenReturn(List.of(leave));

        List<Leave> result = leaveService.getPendingLeaveRequestsByEmployeeId(employeeId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(RequestStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    public void testGetLeaveRequestsByType() {
        LeaveType leaveType = LeaveType.PERSONAL_LEAVE;
        Leave leave = new Leave();
        leave.setLeaveType(leaveType);

        when(leaveRepository.findByLeaveType(leaveType)).thenReturn(List.of(leave));

        List<Leave> result = leaveService.getLeaveRequestsByType(leaveType);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(leaveType, result.get(0).getLeaveType());
    }

    @Test
    public void testGetLeaveRequestsByStatus() {
        RequestStatus status = RequestStatus.PENDING;
        Leave leave = new Leave();
        leave.setStatus(status);

        when(leaveRepository.findByLeaveStatus(status)).thenReturn(List.of(leave));

        List<Leave> result = leaveService.getLeaveRequestsByStatus(status);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(status, result.get(0).getStatus());
    }

    @Test
    public void testGetEmployeesWithPersonalDaysLeft() {
        EmployeeLeaveBalance balance = new EmployeeLeaveBalance();
        balance.setPersonalDaysLeft(5);

        when(leaveBalanceRepository.findEmployeesWithPersonalDaysLeft()).thenReturn(List.of(balance));

        List<EmployeeLeaveBalance> result = leaveService.getEmployeesWithPersonalDaysLeft();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getPersonalDaysLeft() > 0);
    }

    @Test
    public void testGetEmployeesWithSickDaysLeft() {
        EmployeeLeaveBalance balance = new EmployeeLeaveBalance();
        balance.setSickDaysLeft(3);

        when(leaveBalanceRepository.findEmployeesWithSickDaysLeft()).thenReturn(List.of(balance));

        List<EmployeeLeaveBalance> result = leaveService.getEmployeesWithSickDaysLeft();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getSickDaysLeft() > 0);
    }

    @Test
    public void testGetEmployeesWithVacationDaysLeft() {
        EmployeeLeaveBalance balance = new EmployeeLeaveBalance();
        balance.setVacationDaysLeft(7);

        when(leaveBalanceRepository.findEmployeesWithVacationDaysLeft()).thenReturn(List.of(balance));

        List<EmployeeLeaveBalance> result = leaveService.getEmployeesWithVacationDaysLeft();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getVacationDaysLeft() > 0);
    }

    @Test
    public void testGetPendingRequests() {
        Leave leave = new Leave();
        leave.setEmployeeId(1L); // Нужно установить employeeId
        leave.setStatus(RequestStatus.PENDING);
        leave.setLeaveType(LeaveType.PERSONAL_LEAVE);
        EmployeeLeaveBalance balance = new EmployeeLeaveBalance();
        balance.setPersonalDaysLeft(2);
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setName("Test Employee");

        when(leaveRepository.findByPendingStatus(RequestStatus.PENDING)).thenReturn(List.of(leave));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(leaveBalanceRepository.findLeaveBalanceByEmployeeId(anyLong())).thenReturn(balance);

        List<LeaveRequestDto> result = leaveService.getPendingRequests();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetLeaveRequestById() {
        Long leaveId = 1L;
        Leave leave = new Leave();
        leave.setId(leaveId);

        when(leaveRepository.findByLeaveId(leaveId)).thenReturn(leave);

        Leave result = leaveService.getLeaveRequestById(leaveId);

        assertNotNull(result);
        assertEquals(leaveId, result.getId());
    }

    @Test
    public void testApproveLeaveRequest() {
        Long leaveId = 1L;
        Leave leave = new Leave();
        leave.setId(leaveId);
        leave.setEmployeeId(leaveId);
        leave.setLeaveAmount(1);
        leave.setStatus(RequestStatus.PENDING);
        leave.setLeaveType(LeaveType.PERSONAL_LEAVE);
        EmployeeLeaveBalance balance = new EmployeeLeaveBalance();
        balance.setPersonalDaysLeft(1);
        balance.setVacationDaysLeft(1);
        balance.setSickDaysLeft(1);

        when(leaveRepository.findByLeaveId(leaveId)).thenReturn(leave);
        when(leaveRepository.save(any(Leave.class))).thenReturn(leave);
        when(leaveBalanceRepository.findLeaveBalanceByEmployeeId(any(Long.class))).thenReturn(balance);

        Leave result = leaveService.approveLeaveRequest(leaveId);

        assertNotNull(result);
        assertEquals(RequestStatus.APPROVED, result.getStatus());
        assertEquals(balance.getPersonalDaysLeft(), 0);
        verify(leaveRepository, times(1)).save(leave);
    }

    @Test
    public void testApproveLeaveRequest2() {
        Long leaveId = 1L;
        Leave leave = new Leave();
        leave.setId(leaveId);
        leave.setEmployeeId(leaveId);
        leave.setLeaveAmount(1);
        leave.setStatus(RequestStatus.PENDING);
        leave.setLeaveType(LeaveType.SICK_LEAVE);
        EmployeeLeaveBalance balance = new EmployeeLeaveBalance();
        balance.setPersonalDaysLeft(1);
        balance.setVacationDaysLeft(1);
        balance.setSickDaysLeft(1);

        when(leaveRepository.findByLeaveId(leaveId)).thenReturn(leave);
        when(leaveRepository.save(any(Leave.class))).thenReturn(leave);
        when(leaveBalanceRepository.findLeaveBalanceByEmployeeId(any(Long.class))).thenReturn(balance);

        Leave result = leaveService.approveLeaveRequest(leaveId);

        assertNotNull(result);
        assertEquals(RequestStatus.APPROVED, result.getStatus());
        assertEquals(balance.getSickDaysLeft(), 0);
        verify(leaveRepository, times(1)).save(leave);
    }

    @Test
    public void testApproveLeaveRequest3() {
        Long leaveId = 1L;
        Leave leave = new Leave();
        leave.setId(leaveId);
        leave.setEmployeeId(leaveId);
        leave.setLeaveAmount(1);
        leave.setStatus(RequestStatus.PENDING);
        leave.setLeaveType(LeaveType.VACATION_LEAVE);
        EmployeeLeaveBalance balance = new EmployeeLeaveBalance();
        balance.setPersonalDaysLeft(1);
        balance.setVacationDaysLeft(1);
        balance.setSickDaysLeft(1);

        when(leaveRepository.findByLeaveId(leaveId)).thenReturn(leave);
        when(leaveRepository.save(any(Leave.class))).thenReturn(leave);
        when(leaveBalanceRepository.findLeaveBalanceByEmployeeId(any(Long.class))).thenReturn(balance);

        Leave result = leaveService.approveLeaveRequest(leaveId);

        assertNotNull(result);
        assertEquals(RequestStatus.APPROVED, result.getStatus());
        assertEquals(balance.getVacationDaysLeft(), 0);
        verify(leaveRepository, times(1)).save(leave);
    }

    @Test
    public void testRejectLeaveRequest() {
        Long leaveId = 1L;
        Leave leave = new Leave();
        leave.setId(leaveId);
        leave.setStatus(RequestStatus.PENDING);

        when(leaveRepository.findByLeaveId(leaveId)).thenReturn(leave);
        when(leaveRepository.save(any(Leave.class))).thenReturn(leave);

        Leave result = leaveService.rejectLeaveRequest(leaveId);

        assertNotNull(result);
        assertEquals(RequestStatus.REJECTED, result.getStatus());
        verify(leaveRepository, times(1)).save(leave);
    }
}
