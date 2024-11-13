package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE e.team.id = :teamId")
    List<Employee> findByTeamId(Long teamId);
    @Query("SELECT e FROM Employee e WHERE e.team.id = :teamId")
    Page<Employee> findByTeamId(Pageable pageable, Long teamId);

    @Query("SELECT e FROM Employee e JOIN e.currentProjects p WHERE p.projectId = :projectId")
    List<Employee> findEmployeesByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT e FROM Employee e JOIN e.currentProjects p WHERE p.projectId = :projectId")
    Page<Employee> findEmployeesByProjectIdPageable(@Param("projectId") Long projectId, Pageable pageable);
}
