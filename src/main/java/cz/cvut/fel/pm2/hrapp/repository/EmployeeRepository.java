package cz.cvut.fel.pm2.hrapp.repository;
import cz.cvut.fel.pm2.hrapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
