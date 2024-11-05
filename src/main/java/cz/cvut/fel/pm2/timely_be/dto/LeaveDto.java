package cz.cvut.fel.pm2.timely_be.dto;

import cz.cvut.fel.pm2.timely_be.enums.LeaveStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveDto {
    private Long employeeId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus leaveStatus;
    private Integer leaveAmount;
}
