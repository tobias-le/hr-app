package cz.cvut.fel.pm2.timely_be.dto;

import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import lombok.Data;

import java.time.LocalDate;


//používalo se pro posílání nových leave, ne jako návratová hodnota
@Data
public class LeaveDto {
    private Long employeeId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private RequestStatus leaveStatus;
    private Integer leaveAmount;
    private String reason;
}
