package cz.cvut.fel.pm2.hrapp.employeemanagement.repository;

import cz.cvut.fel.pm2.hrapp.employeemanagement.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}