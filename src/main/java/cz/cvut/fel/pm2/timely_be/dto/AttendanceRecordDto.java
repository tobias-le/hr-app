package cz.cvut.fel.pm2.timely_be.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceRecordDto {
    private Long attendanceId;
    private Long memberId;
    private String member;
    private LocalDate date;
    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;
    private String project;
    private String description;
}
