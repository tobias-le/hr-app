package cz.cvut.fel.pm2.hrapp.workinghoursmanagement.model;

import cz.cvut.fel.pm2.hrapp.employeemanagement.model.Employee;
import cz.cvut.fel.pm2.hrapp.workinghoursmanagement.model.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Data
public class WorkHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workHourId;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private BigDecimal hoursWorked; // Always required

    @ManyToOne
    @JoinColumn(name = "supervisorId", nullable = false)
    private Employee supervisor;

    @Enumerated(EnumType.STRING)
    private Status status; // Enum for status (Pending, Approved, Rejected)
}
