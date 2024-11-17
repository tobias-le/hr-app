package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.dto.EmployeeNameWithIdDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT new cz.cvut.fel.pm2.timely_be.dto.EmployeeNameWithIdDto(e.employeeId, e.name) FROM Employee e WHERE e.deleted = false")
    List<EmployeeNameWithIdDto> findAllEmployeeNamesWithIds();

    @Query("SELECT e FROM Employee e WHERE e.team.id = :teamId AND e.deleted = false")
    List<Employee> findByTeamId(Long teamId);
    @Query("SELECT e FROM Employee e WHERE e.team.id = :teamId AND e.deleted = false")
    Page<Employee> findByTeamId(Pageable pageable, Long teamId);

    @Query("SELECT e FROM Employee e JOIN e.currentProjects p WHERE p.projectId = :projectId AND e.deleted = false")
    List<Employee> findEmployeesByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT e FROM Employee e JOIN e.currentProjects p WHERE p.projectId = :projectId AND e.deleted = false")
    Page<Employee> findEmployeesByProjectIdPageable(@Param("projectId") Long projectId, Pageable pageable);

    @Query("SELECT new cz.cvut.fel.pm2.timely_be.dto.EmployeeNameWithIdDto(e.employeeId, e.name) " +
           "FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :namePattern, '%')) " +
           "AND e.employeeId NOT IN :excludeIds AND e.deleted = false")
    List<EmployeeNameWithIdDto> findEmployeesByNameContaining(
        @Param("namePattern") String namePattern,
        @Param("excludeIds") List<Long> excludeIds,
        Pageable pageable);

    @NonNull
    @Override
    @Query("SELECT e FROM Employee e WHERE e.deleted = false")
    List<Employee> findAll();
}
