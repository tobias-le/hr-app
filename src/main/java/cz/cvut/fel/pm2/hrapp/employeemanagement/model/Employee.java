package cz.cvut.fel.pm2.hrapp.employeemanagement.model;

import cz.cvut.fel.pm2.hrapp.employeemanagement.model.enums.ContractType;
import cz.cvut.fel.pm2.hrapp.employeemanagement.model.enums.WorkPercentage;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    private String name;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @Column(nullable = false)
    private Integer contractualHours; // Total hours per week

    @Enumerated(EnumType.STRING)
    private WorkPercentage workPercentage; // Percentage of full-time work

    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "supervisorId")
    private Employee supervisor;

    // The total amount of leave/vacation days the employee has available
    private BigDecimal availableTimeOff;
}