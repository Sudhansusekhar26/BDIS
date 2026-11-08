package com.bdis.enums;

public enum BloodComponentType {
    WHOLE_BLOOD("Whole Blood", 35), // Shelf life in days
    RBC("Packed Red Blood Cells (RBC)", 42),
    PLATELETS("Platelets", 5),
    PLASMA("Fresh Frozen Plasma (FFP)", 365);

    private final String displayName;
    private final int shelfLifeDays;

    BloodComponentType(String displayName, int shelfLifeDays) {
        this.displayName = displayName;
        this.shelfLifeDays = shelfLifeDays;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getShelfLifeDays() {
        return shelfLifeDays;
    }
}
