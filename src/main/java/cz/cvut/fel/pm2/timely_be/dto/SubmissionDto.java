package cz.cvut.fel.pm2.timely_be.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmissionDto {
    private Long messageId;
    private EmployeeNameWithIdDto employee;
    private LocalDateTime datetime;
    private String message;
    private String status;
}