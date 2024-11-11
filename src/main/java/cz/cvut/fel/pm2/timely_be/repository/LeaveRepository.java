package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.enums.LeaveStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import cz.cvut.fel.pm2.timely_be.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    @Query("SELECT l FROM Leave l WHERE l.employeeId = :employeeId AND l.status = cz.cvut.fel.pm2.timely_be.enums.LeaveStatus.PENDING")
    List<Leave> findPendingLeaveRequestsByEmployeeId(Long employeeId);

    @Query("SELECT l FROM Leave l WHERE l.leaveType = :leaveType")
    List<Leave> findByLeaveType(LeaveType leaveType);

    @Query("SELECT l FROM Leave l WHERE l.status = :status")
    List<Leave> findByLeaveStatus(LeaveStatus status);

    @Query("SELECT l FROM Leave l WHERE l.employeeId = :employeeId")
    List<Leave> findLeaveRequestsByEmployeeId(Long employeeId);

    @Query("SELECT l.reason FROM Leave l WHERE l.id = :id")
    String findReasonById(Long id);

    @Query("SELECT l FROM Leave l WHERE l.id = :id")
    Leave findByLeaveId(Long id);

    @Query("SELECT l FROM Leave l WHERE l.status = cz.cvut.fel.pm2.timely_be.enums.LeaveStatus.PENDING")
    List<Leave> findByPendingStatus(LeaveStatus status);
}