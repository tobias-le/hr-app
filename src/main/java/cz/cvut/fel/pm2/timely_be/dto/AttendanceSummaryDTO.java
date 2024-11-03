package cz.cvut.fel.pm2.timely_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendanceSummaryDTO {
    private String teamName;
    private long totalHours;
    private long expectedHours;
    private double averageHoursPerDay;
    private double attendanceRate;
}

