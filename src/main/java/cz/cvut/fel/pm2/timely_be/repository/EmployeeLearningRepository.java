package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.model.EmployeeLearning;
import cz.cvut.fel.pm2.timely_be.repository.composite.EmployeeLearningId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeLearningRepository extends JpaRepository<EmployeeLearning, EmployeeLearningId> {
}
