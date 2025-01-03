package cz.cvut.fel.pm2.timely_be.model;

import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "submissions")
@Data
public class Submission {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long messageId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;
}