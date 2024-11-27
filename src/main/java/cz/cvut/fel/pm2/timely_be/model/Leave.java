package cz.cvut.fel.pm2.timely_be.model;

import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave")
@Data
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "leave_amount", nullable = false)
    private Integer leaveAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_status", nullable = false)
    private RequestStatus status;

    @Column(name="reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;
}
