package cz.cvut.fel.pm2.hrapp.employeemanagement.repository;
import cz.cvut.fel.pm2.hrapp.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
