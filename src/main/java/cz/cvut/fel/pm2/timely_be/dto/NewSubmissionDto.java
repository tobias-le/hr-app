package cz.cvut.fel.pm2.timely_be.dto;

import lombok.Data;

@Data
public class NewSubmissionDto {
    private Long employeeId;
    private String message;
}
