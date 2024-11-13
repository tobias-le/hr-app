package cz.cvut.fel.pm2.timely_be.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "attendance_records")
@Data
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long attendanceId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Employee member;

    private LocalDate date;

    private LocalDateTime clockInTime;

    private LocalDateTime clockOutTime;

    @ManyToOne
    private Project project;

    private String description;

    @Column(nullable = false)
    private boolean deleted = false;
}
