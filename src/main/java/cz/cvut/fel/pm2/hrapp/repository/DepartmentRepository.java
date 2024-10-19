package cz.cvut.fel.pm2.hrapp.repository;

import cz.cvut.fel.pm2.hrapp.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
