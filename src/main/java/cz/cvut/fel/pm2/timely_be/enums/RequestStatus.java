package cz.cvut.fel.pm2.timely_be.enums;

public enum RequestStatus {
    PENDING,
    APPROVED,
    REJECTED;

    public static RequestStatus requestStatusFromString(String string) {
        for (RequestStatus b : RequestStatus.values()) {
            if (b.name().equalsIgnoreCase(string)) {
                return b;
            }
        }
        return null;
    }
}