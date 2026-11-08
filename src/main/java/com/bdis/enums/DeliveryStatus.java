package com.bdis.enums;

public enum DeliveryStatus {
    ASSIGNED("Rider Assigned"),
    IN_TRANSIT("Blood Bag In Transit"),
    DELIVERED("Safely Handed Over"),
    FAILED("Delivery Failed / Cancelled");

    private final String label;

    DeliveryStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
