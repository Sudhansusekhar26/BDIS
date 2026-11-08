package com.bdis.dto;

import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import com.bdis.enums.UrgencyLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BloodRequestCreateDto {

    @NotNull(message = "Target Hospital/Blood Bank is required")
    private Long hospitalId;

    @NotBlank(message = "Patient name is required")
    private String patientName;

    @NotBlank(message = "Hospital where patient is admitted is required")
    private String admittedHospitalName;

    @NotNull(message = "Required blood group is required")
    private BloodGroup bloodGroup;

    @NotNull(message = "Blood component type is required")
    private BloodComponentType componentType = BloodComponentType.WHOLE_BLOOD;

    @NotNull(message = "Units required must be specified")
    @Min(value = 1, message = "At least 1 unit must be requested")
    private Integer unitsRequested;

    @NotNull(message = "Urgency level must be selected")
    private UrgencyLevel urgencyLevel = UrgencyLevel.NORMAL;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    private String prescriptionNotes;

    public BloodRequestCreateDto() {}

    public BloodRequestCreateDto(Long hospitalId, String patientName, String admittedHospitalName,
                                 BloodGroup bloodGroup, BloodComponentType componentType,
                                 Integer unitsRequested, UrgencyLevel urgencyLevel,
                                 String deliveryAddress, String prescriptionNotes) {
        this.hospitalId = hospitalId;
        this.patientName = patientName;
        this.admittedHospitalName = admittedHospitalName;
        this.bloodGroup = bloodGroup;
        this.componentType = componentType != null ? componentType : BloodComponentType.WHOLE_BLOOD;
        this.unitsRequested = unitsRequested;
        this.urgencyLevel = urgencyLevel != null ? urgencyLevel : UrgencyLevel.NORMAL;
        this.deliveryAddress = deliveryAddress;
        this.prescriptionNotes = prescriptionNotes;
    }

    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getAdmittedHospitalName() { return admittedHospitalName; }
    public void setAdmittedHospitalName(String admittedHospitalName) { this.admittedHospitalName = admittedHospitalName; }

    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }

    public BloodComponentType getComponentType() { return componentType; }
    public void setComponentType(BloodComponentType componentType) { this.componentType = componentType; }

    public Integer getUnitsRequested() { return unitsRequested; }
    public void setUnitsRequested(Integer unitsRequested) { this.unitsRequested = unitsRequested; }

    public UrgencyLevel getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPrescriptionNotes() { return prescriptionNotes; }
    public void setPrescriptionNotes(String prescriptionNotes) { this.prescriptionNotes = prescriptionNotes; }

    public static BloodRequestCreateDtoBuilder builder() { return new BloodRequestCreateDtoBuilder(); }

    public static class BloodRequestCreateDtoBuilder {
        private Long hospitalId;
        private String patientName;
        private String admittedHospitalName;
        private BloodGroup bloodGroup;
        private BloodComponentType componentType = BloodComponentType.WHOLE_BLOOD;
        private Integer unitsRequested;
        private UrgencyLevel urgencyLevel = UrgencyLevel.NORMAL;
        private String deliveryAddress;
        private String prescriptionNotes;

        public BloodRequestCreateDtoBuilder hospitalId(Long hospitalId) { this.hospitalId = hospitalId; return this; }
        public BloodRequestCreateDtoBuilder patientName(String patientName) { this.patientName = patientName; return this; }
        public BloodRequestCreateDtoBuilder admittedHospitalName(String admittedHospitalName) { this.admittedHospitalName = admittedHospitalName; return this; }
        public BloodRequestCreateDtoBuilder bloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public BloodRequestCreateDtoBuilder componentType(BloodComponentType componentType) { this.componentType = componentType; return this; }
        public BloodRequestCreateDtoBuilder unitsRequested(Integer unitsRequested) { this.unitsRequested = unitsRequested; return this; }
        public BloodRequestCreateDtoBuilder urgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; return this; }
        public BloodRequestCreateDtoBuilder deliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; return this; }
        public BloodRequestCreateDtoBuilder prescriptionNotes(String prescriptionNotes) { this.prescriptionNotes = prescriptionNotes; return this; }

        public BloodRequestCreateDto build() {
            return new BloodRequestCreateDto(hospitalId, patientName, admittedHospitalName, bloodGroup, componentType, unitsRequested, urgencyLevel, deliveryAddress, prescriptionNotes);
        }
    }
}
