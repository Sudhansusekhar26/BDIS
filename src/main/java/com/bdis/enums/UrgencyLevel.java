package com.bdis.enums;

public enum UrgencyLevel {
    NORMAL("Normal", "badge-info", "Within 24-48 Hours"),
    CRITICAL("Critical", "badge-warning", "Within 4-6 Hours"),
    EMERGENCY("Emergency / SOS", "badge-danger", "Immediate / Under 1 Hour");

    private final String label;
    private final String badgeClass;
    private final String etaDescription;

    UrgencyLevel(String label, String badgeClass, String etaDescription) {
        this.label = label;
        this.badgeClass = badgeClass;
        this.etaDescription = etaDescription;
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public String getEtaDescription() {
        return etaDescription;
    }
}
