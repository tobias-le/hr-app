package cz.cvut.fel.pm2.timely_be.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class EmployeeLeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long employeeId;
    private Integer vacationDaysLeft;
    private Integer sickDaysLeft;
    private Integer personalDaysLeft;
}
