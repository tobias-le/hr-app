package cz.cvut.fel.pm2.timely_be.enums;

import lombok.Getter;

@Getter
public enum EmploymentType {
    FULL_TIME(8),
    PART_TIME(4),
    CONTRACT(0);

    private final long expectedHoursPerDay;

    EmploymentType(long expectedHoursPerDay) {
        this.expectedHoursPerDay = expectedHoursPerDay;
    }

    public static EmploymentType fromString(String text) {
        for (EmploymentType b : EmploymentType.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

}