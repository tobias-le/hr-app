package cz.cvut.fel.pm2.hrapp.hoursmanagement.repository;

import cz.cvut.fel.pm2.hrapp.hoursmanagement.model.TimeCell;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeCellRepository extends JpaRepository<TimeCell, Long> {

}
