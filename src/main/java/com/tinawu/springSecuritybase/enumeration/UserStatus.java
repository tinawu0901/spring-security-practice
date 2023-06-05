	package com.tinawu.springSecuritybase.enumeration;

public enum UserStatus {
    ACTIVE("ACTIVE"),
    DISABLED("DISABLED"),
    DELETED("DELETED");

    private String status;

    UserStatus(final String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
