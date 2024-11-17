package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import cz.cvut.fel.pm2.timely_be.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    @Query("SELECT l FROM Leave l WHERE l.employeeId = :employeeId AND l.status = cz.cvut.fel.pm2.timely_be.enums.RequestStatus.PENDING")
    List<Leave> findPendingLeaveRequestsByEmployeeId(Long employeeId);

    @Query("SELECT l FROM Leave l WHERE l.leaveType = :leaveType")
    List<Leave> findByLeaveType(LeaveType leaveType);

    @Query("SELECT l FROM Leave l WHERE l.status = :status")
    List<Leave> findByLeaveStatus(RequestStatus status);

    @Query("SELECT l FROM Leave l WHERE l.employeeId = :employeeId")
    List<Leave> findLeaveRequestsByEmployeeId(Long employeeId);

    @Query("SELECT l.reason FROM Leave l WHERE l.id = :id")
    String findReasonById(Long id);

    @Query("SELECT l FROM Leave l WHERE l.id = :id")
    Leave findByLeaveId(Long id);

    @Query("SELECT l FROM Leave l WHERE l.status = cz.cvut.fel.pm2.timely_be.enums.RequestStatus.PENDING")
    List<Leave> findByPendingStatus(RequestStatus status);
}