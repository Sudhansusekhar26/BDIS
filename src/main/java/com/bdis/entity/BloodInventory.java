package com.bdis.entity;

import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "blood_inventory",
    indexes = {
        @Index(name = "idx_inventory_lookup", columnList = "blood_group, component_type, units_available"),
        @Index(name = "idx_inventory_hospital", columnList = "hospital_id")
    },
    uniqueConstraints = @UniqueConstraint(columnNames = {"hospital_id", "blood_group", "component_type", "batch_number"})
)
public class BloodInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalProfile hospital;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", nullable = false, length = 20)
    private BloodGroup bloodGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false, length = 30)
    private BloodComponentType componentType;

    @Column(name = "units_available", nullable = false)
    private int unitsAvailable = 0;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "batch_number", nullable = false, length = 100)
    private String batchNumber;

    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;

    @UpdateTimestamp
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    public BloodInventory() {}

    public BloodInventory(Long id, HospitalProfile hospital, BloodGroup bloodGroup, BloodComponentType componentType,
                          int unitsAvailable, LocalDate expiryDate, String batchNumber, Long version, LocalDateTime lastUpdated) {
        this.id = id;
        this.hospital = hospital;
        this.bloodGroup = bloodGroup;
        this.componentType = componentType;
        this.unitsAvailable = unitsAvailable;
        this.expiryDate = expiryDate;
        this.batchNumber = batchNumber;
        this.version = version != null ? version : 0L;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public HospitalProfile getHospital() { return hospital; }
    public void setHospital(HospitalProfile hospital) { this.hospital = hospital; }

    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }

    public BloodComponentType getComponentType() { return componentType; }
    public void setComponentType(BloodComponentType componentType) { this.componentType = componentType; }

    public int getUnitsAvailable() { return unitsAvailable; }
    public void setUnitsAvailable(int unitsAvailable) { this.unitsAvailable = unitsAvailable; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    public boolean isLowStock() {
        return unitsAvailable > 0 && unitsAvailable <= 5;
    }

    public boolean isOutOfStock() {
        return unitsAvailable <= 0;
    }

    public static BloodInventoryBuilder builder() { return new BloodInventoryBuilder(); }

    public static class BloodInventoryBuilder {
        private Long id;
        private HospitalProfile hospital;
        private BloodGroup bloodGroup;
        private BloodComponentType componentType;
        private int unitsAvailable = 0;
        private LocalDate expiryDate;
        private String batchNumber;
        private Long version = 0L;
        private LocalDateTime lastUpdated;

        public BloodInventoryBuilder id(Long id) { this.id = id; return this; }
        public BloodInventoryBuilder hospital(HospitalProfile hospital) { this.hospital = hospital; return this; }
        public BloodInventoryBuilder bloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public BloodInventoryBuilder componentType(BloodComponentType componentType) { this.componentType = componentType; return this; }
        public BloodInventoryBuilder unitsAvailable(int unitsAvailable) { this.unitsAvailable = unitsAvailable; return this; }
        public BloodInventoryBuilder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public BloodInventoryBuilder batchNumber(String batchNumber) { this.batchNumber = batchNumber; return this; }
        public BloodInventoryBuilder version(Long version) { this.version = version; return this; }
        public BloodInventoryBuilder lastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; return this; }

        public BloodInventory build() {
            return new BloodInventory(id, hospital, bloodGroup, componentType, unitsAvailable, expiryDate, batchNumber, version, lastUpdated);
        }
    }
}
