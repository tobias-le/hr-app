package cz.cvut.fel.pm2.timely_be.dto;

import cz.cvut.fel.pm2.timely_be.enums.LeaveStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SubmissionDto {
    private Long messageId;
    private EmployeeNameWithIdDto employee;
    private LocalDateTime datetime;
    private LeaveStatus status;
    private String message;
    private LocalDateTime requestCreatedDate;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer remainingLeaveDays;
}