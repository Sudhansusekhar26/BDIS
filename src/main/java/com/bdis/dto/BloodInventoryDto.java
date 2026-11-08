package com.bdis.dto;

import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BloodInventoryDto {

    private Long id;
    private Long hospitalId;
    private String hospitalName;
    private String hospitalHelpline;
    private String hospitalAddress;
    private String districtName;
    private String stateName;
    private BloodGroup bloodGroup;
    private String bloodGroupLabel;
    private BloodComponentType componentType;
    private String componentName;
    private int unitsAvailable;
    private LocalDate expiryDate;
    private String batchNumber;
    private boolean lowStock;
    private boolean outOfStock;
    private boolean expired;
    private LocalDateTime lastUpdated;

    public BloodInventoryDto() {}

    public BloodInventoryDto(Long id, Long hospitalId, String hospitalName, String hospitalHelpline,
                             String hospitalAddress, String districtName, String stateName,
                             BloodGroup bloodGroup, String bloodGroupLabel, BloodComponentType componentType,
                             String componentName, int unitsAvailable, LocalDate expiryDate,
                             String batchNumber, boolean lowStock, boolean outOfStock, boolean expired,
                             LocalDateTime lastUpdated) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.hospitalHelpline = hospitalHelpline;
        this.hospitalAddress = hospitalAddress;
        this.districtName = districtName;
        this.stateName = stateName;
        this.bloodGroup = bloodGroup;
        this.bloodGroupLabel = bloodGroupLabel;
        this.componentType = componentType;
        this.componentName = componentName;
        this.unitsAvailable = unitsAvailable;
        this.expiryDate = expiryDate;
        this.batchNumber = batchNumber;
        this.lowStock = lowStock;
        this.outOfStock = outOfStock;
        this.expired = expired;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getHospitalHelpline() { return hospitalHelpline; }
    public void setHospitalHelpline(String hospitalHelpline) { this.hospitalHelpline = hospitalHelpline; }

    public String getHospitalAddress() { return hospitalAddress; }
    public void setHospitalAddress(String hospitalAddress) { this.hospitalAddress = hospitalAddress; }

    public String getDistrictName() { return districtName; }
    public void setDistrictName(String districtName) { this.districtName = districtName; }

    public String getStateName() { return stateName; }
    public void setStateName(String stateName) { this.stateName = stateName; }

    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getBloodGroupLabel() { return bloodGroupLabel; }
    public void setBloodGroupLabel(String bloodGroupLabel) { this.bloodGroupLabel = bloodGroupLabel; }

    public BloodComponentType getComponentType() { return componentType; }
    public void setComponentType(BloodComponentType componentType) { this.componentType = componentType; }

    public String getComponentName() { return componentName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }

    public int getUnitsAvailable() { return unitsAvailable; }
    public void setUnitsAvailable(int unitsAvailable) { this.unitsAvailable = unitsAvailable; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public boolean isLowStock() { return lowStock; }
    public void setLowStock(boolean lowStock) { this.lowStock = lowStock; }

    public boolean isOutOfStock() { return outOfStock; }
    public void setOutOfStock(boolean outOfStock) { this.outOfStock = outOfStock; }

    public boolean isExpired() { return expired; }
    public void setExpired(boolean expired) { this.expired = expired; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public static BloodInventoryDtoBuilder builder() { return new BloodInventoryDtoBuilder(); }

    public static class BloodInventoryDtoBuilder {
        private Long id;
        private Long hospitalId;
        private String hospitalName;
        private String hospitalHelpline;
        private String hospitalAddress;
        private String districtName;
        private String stateName;
        private BloodGroup bloodGroup;
        private String bloodGroupLabel;
        private BloodComponentType componentType;
        private String componentName;
        private int unitsAvailable;
        private LocalDate expiryDate;
        private String batchNumber;
        private boolean lowStock;
        private boolean outOfStock;
        private boolean expired;
        private LocalDateTime lastUpdated;

        public BloodInventoryDtoBuilder id(Long id) { this.id = id; return this; }
        public BloodInventoryDtoBuilder hospitalId(Long hospitalId) { this.hospitalId = hospitalId; return this; }
        public BloodInventoryDtoBuilder hospitalName(String hospitalName) { this.hospitalName = hospitalName; return this; }
        public BloodInventoryDtoBuilder hospitalHelpline(String hospitalHelpline) { this.hospitalHelpline = hospitalHelpline; return this; }
        public BloodInventoryDtoBuilder hospitalAddress(String hospitalAddress) { this.hospitalAddress = hospitalAddress; return this; }
        public BloodInventoryDtoBuilder districtName(String districtName) { this.districtName = districtName; return this; }
        public BloodInventoryDtoBuilder stateName(String stateName) { this.stateName = stateName; return this; }
        public BloodInventoryDtoBuilder bloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public BloodInventoryDtoBuilder bloodGroupLabel(String bloodGroupLabel) { this.bloodGroupLabel = bloodGroupLabel; return this; }
        public BloodInventoryDtoBuilder componentType(BloodComponentType componentType) { this.componentType = componentType; return this; }
        public BloodInventoryDtoBuilder componentName(String componentName) { this.componentName = componentName; return this; }
        public BloodInventoryDtoBuilder unitsAvailable(int unitsAvailable) { this.unitsAvailable = unitsAvailable; return this; }
        public BloodInventoryDtoBuilder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public BloodInventoryDtoBuilder batchNumber(String batchNumber) { this.batchNumber = batchNumber; return this; }
        public BloodInventoryDtoBuilder lowStock(boolean lowStock) { this.lowStock = lowStock; return this; }
        public BloodInventoryDtoBuilder outOfStock(boolean outOfStock) { this.outOfStock = outOfStock; return this; }
        public BloodInventoryDtoBuilder expired(boolean expired) { this.expired = expired; return this; }
        public BloodInventoryDtoBuilder lastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; return this; }

        public BloodInventoryDto build() {
            return new BloodInventoryDto(id, hospitalId, hospitalName, hospitalHelpline, hospitalAddress,
                    districtName, stateName, bloodGroup, bloodGroupLabel, componentType, componentName,
                    unitsAvailable, expiryDate, batchNumber, lowStock, outOfStock, expired, lastUpdated);
        }
    }
}
