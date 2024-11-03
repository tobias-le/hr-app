package cz.cvut.fel.pm2.timely_be.enums;

import lombok.Getter;

@Getter
public enum EmploymentStatus {
    FULL_TIME(8),
    PART_TIME(4),
    CONTRACT(8);

    private final long expectedHoursPerDay;

    EmploymentStatus(long expectedHoursPerDay) {
        this.expectedHoursPerDay = expectedHoursPerDay;
    }

}