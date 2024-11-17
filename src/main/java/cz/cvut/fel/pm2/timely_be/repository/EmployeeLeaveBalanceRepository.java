package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.model.EmployeeLeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeLeaveBalanceRepository extends JpaRepository<EmployeeLeaveBalance, Long> {
    @Query("SELECT e FROM EmployeeLeaveBalance e WHERE e.personalDaysLeft > 0")
    List<EmployeeLeaveBalance> findEmployeesWithPersonalDaysLeft();

    @Query("SELECT e FROM EmployeeLeaveBalance e WHERE e.sickDaysLeft > 0")
    List<EmployeeLeaveBalance> findEmployeesWithSickDaysLeft();

    @Query("SELECT e FROM EmployeeLeaveBalance e WHERE e.vacationDaysLeft > 0")
    List<EmployeeLeaveBalance> findEmployeesWithVacationDaysLeft();

    @Query("SELECT e FROM EmployeeLeaveBalance e WHERE e.employeeId = :employeeId")
    EmployeeLeaveBalance findLeaveBalanceByEmployeeId(Long employeeId);
}