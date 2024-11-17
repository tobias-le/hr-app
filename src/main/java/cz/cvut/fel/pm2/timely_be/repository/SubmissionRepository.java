package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import cz.cvut.fel.pm2.timely_be.enums.SubmissionStatus;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("SELECT s FROM Submission s WHERE s.status = 'PENDING' ORDER BY s.datetime DESC")
    List<Submission> findAllPendingOrderByDatetimeDesc();

    @Query("SELECT s FROM Submission s WHERE s.employeeId = :employeeId ORDER BY s.datetime DESC")
    List<Submission> findAllByEmployeeIdOrderByDatetimeDesc(Long employeeId);
}