package com.bdis.dto;

import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class StockUpdateDto {

    private Long id;

    @NotNull(message = "Blood group is required")
    private BloodGroup bloodGroup;

    @NotNull(message = "Component type is required")
    private BloodComponentType componentType;

    @NotNull(message = "Units available must be specified")
    @Min(value = 0, message = "Units cannot be negative")
    private Integer unitsAvailable;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expiryDate;

    @NotBlank(message = "Batch number is required")
    private String batchNumber;

    private String operation = "SET";
    private Integer deltaUnits;

    public StockUpdateDto() {}

    public StockUpdateDto(Long id, BloodGroup bloodGroup, BloodComponentType componentType, Integer unitsAvailable, LocalDate expiryDate, String batchNumber, String operation, Integer deltaUnits) {
        this.id = id;
        this.bloodGroup = bloodGroup;
        this.componentType = componentType;
        this.unitsAvailable = unitsAvailable;
        this.expiryDate = expiryDate;
        this.batchNumber = batchNumber;
        this.operation = operation != null ? operation : "SET";
        this.deltaUnits = deltaUnits;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }

    public BloodComponentType getComponentType() { return componentType; }
    public void setComponentType(BloodComponentType componentType) { this.componentType = componentType; }

    public Integer getUnitsAvailable() { return unitsAvailable; }
    public void setUnitsAvailable(Integer unitsAvailable) { this.unitsAvailable = unitsAvailable; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public Integer getDeltaUnits() { return deltaUnits; }
    public void setDeltaUnits(Integer deltaUnits) { this.deltaUnits = deltaUnits; }

    public static StockUpdateDtoBuilder builder() { return new StockUpdateDtoBuilder(); }

    public static class StockUpdateDtoBuilder {
        private Long id;
        private BloodGroup bloodGroup;
        private BloodComponentType componentType;
        private Integer unitsAvailable;
        private LocalDate expiryDate;
        private String batchNumber;
        private String operation = "SET";
        private Integer deltaUnits;

        public StockUpdateDtoBuilder id(Long id) { this.id = id; return this; }
        public StockUpdateDtoBuilder bloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public StockUpdateDtoBuilder componentType(BloodComponentType componentType) { this.componentType = componentType; return this; }
        public StockUpdateDtoBuilder unitsAvailable(Integer unitsAvailable) { this.unitsAvailable = unitsAvailable; return this; }
        public StockUpdateDtoBuilder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public StockUpdateDtoBuilder batchNumber(String batchNumber) { this.batchNumber = batchNumber; return this; }
        public StockUpdateDtoBuilder operation(String operation) { this.operation = operation; return this; }
        public StockUpdateDtoBuilder deltaUnits(Integer deltaUnits) { this.deltaUnits = deltaUnits; return this; }

        public StockUpdateDto build() {
            return new StockUpdateDto(id, bloodGroup, componentType, unitsAvailable, expiryDate, batchNumber, operation, deltaUnits);
        }
    }
}
