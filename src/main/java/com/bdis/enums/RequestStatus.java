package com.bdis.enums;

public enum RequestStatus {
    PENDING("Pending Verification", "bg-warning text-dark", 1),
    ACCEPTED("Accepted / Reserved", "bg-primary text-white", 2),
    DISPATCHED("Dispatched / In-Transit", "bg-info text-dark", 3),
    DELIVERED("Delivered / Completed", "bg-success text-white", 4),
    REJECTED("Rejected", "bg-danger text-white", 0),
    CANCELLED("Cancelled", "bg-secondary text-white", 0);

    private final String label;
    private final String badgeClass;
    private final int stepIndex;

    RequestStatus(String label, String badgeClass, int stepIndex) {
        this.label = label;
        this.badgeClass = badgeClass;
        this.stepIndex = stepIndex;
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public int getStepIndex() {
        return stepIndex;
    }
}
