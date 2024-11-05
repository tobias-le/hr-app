package cz.cvut.fel.pm2.timely_be.model;

import jakarta.persistence.*;


@Entity
public class EmployeeLeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employeeId")
    private Employee employeeId;

    private Integer vacationDaysLeft;
    private Integer sickDaysLeft;
    private Integer personalDaysLeft;

}
