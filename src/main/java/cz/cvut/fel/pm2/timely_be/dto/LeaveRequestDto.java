package cz.cvut.fel.pm2.timely_be.dto;

import cz.cvut.fel.pm2.timely_be.enums.LeaveType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveRequestDto extends SubmissionDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveType leaveType;
    private Number leaveAmount;
}
