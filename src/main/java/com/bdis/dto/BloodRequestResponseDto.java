package com.bdis.dto;

import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import com.bdis.enums.DeliveryStatus;
import com.bdis.enums.RequestStatus;
import com.bdis.enums.UrgencyLevel;

import java.time.LocalDateTime;

public class BloodRequestResponseDto {

    private Long id;
    private Long requesterId;
    private String requesterName;
    private String requesterPhone;
    private String requesterEmail;

    private Long hospitalId;
    private String hospitalName;
    private String hospitalHelpline;
    private String hospitalAddress;

    private String patientName;
    private String admittedHospitalName;
    private BloodGroup bloodGroup;
    private String bloodGroupLabel;
    private BloodComponentType componentType;
    private String componentName;
    private int unitsRequested;
    private UrgencyLevel urgencyLevel;
    private RequestStatus status;
    private String statusBadgeClass;
    private String deliveryAddress;
    private String prescriptionNotes;
    private String rejectionReason;

    // Delivery fields
    private Long deliveryId;
    private String deliveryPartnerName;
    private String deliveryContact;
    private String vehicleNumber;
    private String trackingNotes;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime dispatchedAt;
    private LocalDateTime deliveredAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BloodRequestResponseDto() {}

    public BloodRequestResponseDto(Long id, Long requesterId, String requesterName, String requesterPhone, String requesterEmail,
                                   Long hospitalId, String hospitalName, String hospitalHelpline, String hospitalAddress,
                                   String patientName, String admittedHospitalName, BloodGroup bloodGroup, String bloodGroupLabel,
                                   BloodComponentType componentType, String componentName, int unitsRequested,
                                   UrgencyLevel urgencyLevel, RequestStatus status, String statusBadgeClass,
                                   String deliveryAddress, String prescriptionNotes, String rejectionReason,
                                   Long deliveryId, String deliveryPartnerName, String deliveryContact,
                                   String vehicleNumber, String trackingNotes, DeliveryStatus deliveryStatus,
                                   LocalDateTime dispatchedAt, LocalDateTime deliveredAt,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.requesterId = requesterId;
        this.requesterName = requesterName;
        this.requesterPhone = requesterPhone;
        this.requesterEmail = requesterEmail;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.hospitalHelpline = hospitalHelpline;
        this.hospitalAddress = hospitalAddress;
        this.patientName = patientName;
        this.admittedHospitalName = admittedHospitalName;
        this.bloodGroup = bloodGroup;
        this.bloodGroupLabel = bloodGroupLabel;
        this.componentType = componentType;
        this.componentName = componentName;
        this.unitsRequested = unitsRequested;
        this.urgencyLevel = urgencyLevel;
        this.status = status;
        this.statusBadgeClass = statusBadgeClass;
        this.deliveryAddress = deliveryAddress;
        this.prescriptionNotes = prescriptionNotes;
        this.rejectionReason = rejectionReason;
        this.deliveryId = deliveryId;
        this.deliveryPartnerName = deliveryPartnerName;
        this.deliveryContact = deliveryContact;
        this.vehicleNumber = vehicleNumber;
        this.trackingNotes = trackingNotes;
        this.deliveryStatus = deliveryStatus;
        this.dispatchedAt = dispatchedAt;
        this.deliveredAt = deliveredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRequesterId() { return requesterId; }
    public void setRequesterId(Long requesterId) { this.requesterId = requesterId; }

    public String getRequesterName() { return requesterName; }
    public void setRequesterName(String requesterName) { this.requesterName = requesterName; }

    public String getRequesterPhone() { return requesterPhone; }
    public void setRequesterPhone(String requesterPhone) { this.requesterPhone = requesterPhone; }

    public String getRequesterEmail() { return requesterEmail; }
    public void setRequesterEmail(String requesterEmail) { this.requesterEmail = requesterEmail; }

    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getHospitalHelpline() { return hospitalHelpline; }
    public void setHospitalHelpline(String hospitalHelpline) { this.hospitalHelpline = hospitalHelpline; }

    public String getHospitalAddress() { return hospitalAddress; }
    public void setHospitalAddress(String hospitalAddress) { this.hospitalAddress = hospitalAddress; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getAdmittedHospitalName() { return admittedHospitalName; }
    public void setAdmittedHospitalName(String admittedHospitalName) { this.admittedHospitalName = admittedHospitalName; }

    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getBloodGroupLabel() { return bloodGroupLabel; }
    public void setBloodGroupLabel(String bloodGroupLabel) { this.bloodGroupLabel = bloodGroupLabel; }

    public BloodComponentType getComponentType() { return componentType; }
    public void setComponentType(BloodComponentType componentType) { this.componentType = componentType; }

    public String getComponentName() { return componentName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }

    public int getUnitsRequested() { return unitsRequested; }
    public void setUnitsRequested(int unitsRequested) { this.unitsRequested = unitsRequested; }

    public UrgencyLevel getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public String getStatusBadgeClass() { return statusBadgeClass; }
    public void setStatusBadgeClass(String statusBadgeClass) { this.statusBadgeClass = statusBadgeClass; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPrescriptionNotes() { return prescriptionNotes; }
    public void setPrescriptionNotes(String prescriptionNotes) { this.prescriptionNotes = prescriptionNotes; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }

    public String getDeliveryPartnerName() { return deliveryPartnerName; }
    public void setDeliveryPartnerName(String deliveryPartnerName) { this.deliveryPartnerName = deliveryPartnerName; }

    public String getDeliveryContact() { return deliveryContact; }
    public void setDeliveryContact(String deliveryContact) { this.deliveryContact = deliveryContact; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getTrackingNotes() { return trackingNotes; }
    public void setTrackingNotes(String trackingNotes) { this.trackingNotes = trackingNotes; }

    public DeliveryStatus getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(DeliveryStatus deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public LocalDateTime getDispatchedAt() { return dispatchedAt; }
    public void setDispatchedAt(LocalDateTime dispatchedAt) { this.dispatchedAt = dispatchedAt; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static BloodRequestResponseDtoBuilder builder() { return new BloodRequestResponseDtoBuilder(); }

    public static class BloodRequestResponseDtoBuilder {
        private Long id;
        private Long requesterId;
        private String requesterName;
        private String requesterPhone;
        private String requesterEmail;
        private Long hospitalId;
        private String hospitalName;
        private String hospitalHelpline;
        private String hospitalAddress;
        private String patientName;
        private String admittedHospitalName;
        private BloodGroup bloodGroup;
        private String bloodGroupLabel;
        private BloodComponentType componentType;
        private String componentName;
        private int unitsRequested;
        private UrgencyLevel urgencyLevel;
        private RequestStatus status;
        private String statusBadgeClass;
        private String deliveryAddress;
        private String prescriptionNotes;
        private String rejectionReason;
        private Long deliveryId;
        private String deliveryPartnerName;
        private String deliveryContact;
        private String vehicleNumber;
        private String trackingNotes;
        private DeliveryStatus deliveryStatus;
        private LocalDateTime dispatchedAt;
        private LocalDateTime deliveredAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public BloodRequestResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public BloodRequestResponseDtoBuilder requesterId(Long requesterId) { this.requesterId = requesterId; return this; }
        public BloodRequestResponseDtoBuilder requesterName(String requesterName) { this.requesterName = requesterName; return this; }
        public BloodRequestResponseDtoBuilder requesterPhone(String requesterPhone) { this.requesterPhone = requesterPhone; return this; }
        public BloodRequestResponseDtoBuilder requesterEmail(String requesterEmail) { this.requesterEmail = requesterEmail; return this; }
        public BloodRequestResponseDtoBuilder hospitalId(Long hospitalId) { this.hospitalId = hospitalId; return this; }
        public BloodRequestResponseDtoBuilder hospitalName(String hospitalName) { this.hospitalName = hospitalName; return this; }
        public BloodRequestResponseDtoBuilder hospitalHelpline(String hospitalHelpline) { this.hospitalHelpline = hospitalHelpline; return this; }
        public BloodRequestResponseDtoBuilder hospitalAddress(String hospitalAddress) { this.hospitalAddress = hospitalAddress; return this; }
        public BloodRequestResponseDtoBuilder patientName(String patientName) { this.patientName = patientName; return this; }
        public BloodRequestResponseDtoBuilder admittedHospitalName(String admittedHospitalName) { this.admittedHospitalName = admittedHospitalName; return this; }
        public BloodRequestResponseDtoBuilder bloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public BloodRequestResponseDtoBuilder bloodGroupLabel(String bloodGroupLabel) { this.bloodGroupLabel = bloodGroupLabel; return this; }
        public BloodRequestResponseDtoBuilder componentType(BloodComponentType componentType) { this.componentType = componentType; return this; }
        public BloodRequestResponseDtoBuilder componentName(String componentName) { this.componentName = componentName; return this; }
        public BloodRequestResponseDtoBuilder unitsRequested(int unitsRequested) { this.unitsRequested = unitsRequested; return this; }
        public BloodRequestResponseDtoBuilder urgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; return this; }
        public BloodRequestResponseDtoBuilder status(RequestStatus status) { this.status = status; return this; }
        public BloodRequestResponseDtoBuilder statusBadgeClass(String statusBadgeClass) { this.statusBadgeClass = statusBadgeClass; return this; }
        public BloodRequestResponseDtoBuilder deliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; return this; }
        public BloodRequestResponseDtoBuilder prescriptionNotes(String prescriptionNotes) { this.prescriptionNotes = prescriptionNotes; return this; }
        public BloodRequestResponseDtoBuilder rejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; return this; }
        public BloodRequestResponseDtoBuilder deliveryId(Long deliveryId) { this.deliveryId = deliveryId; return this; }
        public BloodRequestResponseDtoBuilder deliveryPartnerName(String deliveryPartnerName) { this.deliveryPartnerName = deliveryPartnerName; return this; }
        public BloodRequestResponseDtoBuilder deliveryContact(String deliveryContact) { this.deliveryContact = deliveryContact; return this; }
        public BloodRequestResponseDtoBuilder vehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; return this; }
        public BloodRequestResponseDtoBuilder trackingNotes(String trackingNotes) { this.trackingNotes = trackingNotes; return this; }
        public BloodRequestResponseDtoBuilder deliveryStatus(DeliveryStatus deliveryStatus) { this.deliveryStatus = deliveryStatus; return this; }
        public BloodRequestResponseDtoBuilder dispatchedAt(LocalDateTime dispatchedAt) { this.dispatchedAt = dispatchedAt; return this; }
        public BloodRequestResponseDtoBuilder deliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; return this; }
        public BloodRequestResponseDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public BloodRequestResponseDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public BloodRequestResponseDto build() {
            return new BloodRequestResponseDto(id, requesterId, requesterName, requesterPhone, requesterEmail,
                    hospitalId, hospitalName, hospitalHelpline, hospitalAddress,
                    patientName, admittedHospitalName, bloodGroup, bloodGroupLabel, componentType, componentName, unitsRequested,
                    urgencyLevel, status, statusBadgeClass, deliveryAddress, prescriptionNotes, rejectionReason,
                    deliveryId, deliveryPartnerName, deliveryContact, vehicleNumber, trackingNotes, deliveryStatus,
                    dispatchedAt, deliveredAt, createdAt, updatedAt);
        }
    }
}
