package com.bdis.enums;

import java.util.Arrays;
import java.util.List;

public enum BloodGroup {
    A_POS("A+"),
    A_NEG("A-"),
    B_POS("B+"),
    B_NEG("B-"),
    AB_POS("AB+"),
    AB_NEG("AB-"),
    O_POS("O+"),
    O_NEG("O-");

    private final String label;

    BloodGroup(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static BloodGroup fromLabel(String label) {
        if (label == null || label.trim().isEmpty()) {
            return null;
        }
        String clean = label.trim().toUpperCase();
        for (BloodGroup bg : values()) {
            if (bg.name().equalsIgnoreCase(clean) || bg.label.equalsIgnoreCase(clean)) {
                return bg;
            }
        }
        return null;
    }

    /**
     * Returns compatible donor blood groups for this recipient blood group.
     */
    public List<BloodGroup> getCompatibleDonorGroups() {
        return switch (this) {
            case A_POS -> Arrays.asList(A_POS, A_NEG, O_POS, O_NEG);
            case A_NEG -> Arrays.asList(A_NEG, O_NEG);
            case B_POS -> Arrays.asList(B_POS, B_NEG, O_POS, O_NEG);
            case B_NEG -> Arrays.asList(B_NEG, O_NEG);
            case AB_POS -> Arrays.asList(values()); // Universal Recipient
            case AB_NEG -> Arrays.asList(AB_NEG, A_NEG, B_NEG, O_NEG);
            case O_POS -> Arrays.asList(O_POS, O_NEG);
            case O_NEG -> Arrays.asList(O_NEG); // Universal Donor
        };
    }
}
