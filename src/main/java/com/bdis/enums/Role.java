package com.bdis.enums;

public enum Role {
    ROLE_ADMIN("Administrator"),
    ROLE_HOSPITAL("Hospital / Blood Bank"),
    ROLE_USER("User / Donor / Requester");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
