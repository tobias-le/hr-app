package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("SELECT s FROM Submission s WHERE s.status = cz.cvut.fel.pm2.timely_be.enums.RequestStatus.PENDING ORDER BY s.datetime DESC")
    List<Submission> findPendingSubmissions();

    @Query("SELECT s FROM Submission s WHERE s.employeeId = :employeeId ORDER BY s.datetime DESC")
    List<Submission> findSubmissionsByEmployeeId(@Param("employeeId") Long employeeId);
}