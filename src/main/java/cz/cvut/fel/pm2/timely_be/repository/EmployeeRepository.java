package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE e.team.id = :teamId")
    List<Employee> findByTeamId(Long teamId);
    @Query("SELECT e FROM Employee e WHERE e.team.id = :teamId")
    Page<Employee> findByTeamId(Pageable pageable, Long teamId);
    @Query("SELECT e FROM Employee e WHERE e.id = :employeeId")
    Optional<Employee> findById(Long employeeId);
}
