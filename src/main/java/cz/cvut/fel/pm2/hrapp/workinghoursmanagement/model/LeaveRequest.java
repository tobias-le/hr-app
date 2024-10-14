package cz.cvut.fel.pm2.hrapp.workinghoursmanagement.model;

import cz.cvut.fel.pm2.hrapp.employeemanagement.model.Employee;
import cz.cvut.fel.pm2.hrapp.workinghoursmanagement.model.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String leaveType; // Vacation, Sick Leave, etc.

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private Status status; // Enum for status (Pending, Approved, Rejected)

    private String reason;

    @ManyToOne
    @JoinColumn(name = "supervisorId", nullable = false)
    private Employee supervisor;
}
