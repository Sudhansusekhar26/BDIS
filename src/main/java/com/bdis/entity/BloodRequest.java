package com.bdis.entity;

import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import com.bdis.enums.RequestStatus;
import com.bdis.enums.UrgencyLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "blood_requests",
    indexes = {
        @Index(name = "idx_requests_status", columnList = "status"),
        @Index(name = "idx_requests_hospital", columnList = "hospital_id, status"),
        @Index(name = "idx_requests_requester", columnList = "requester_id")
    }
)
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalProfile hospital;

    @Column(name = "patient_name", nullable = false, length = 150)
    private String patientName;

    @Column(name = "admitted_hospital_name", nullable = false, length = 200)
    private String admittedHospitalName;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", nullable = false, length = 20)
    private BloodGroup bloodGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false, length = 30)
    private BloodComponentType componentType = BloodComponentType.WHOLE_BLOOD;

    @Column(name = "units_requested", nullable = false)
    private int unitsRequested;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level", nullable = false, length = 30)
    private UrgencyLevel urgencyLevel = UrgencyLevel.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(name = "delivery_address", nullable = false, length = 300)
    private String deliveryAddress;

    @Column(name = "prescription_notes", columnDefinition = "TEXT")
    private String prescriptionNotes;

    @Column(name = "rejection_reason", length = 300)
    private String rejectionReason;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BloodDelivery delivery;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public BloodRequest() {}

    public BloodRequest(Long id, User requester, HospitalProfile hospital, String patientName,
                        String admittedHospitalName, BloodGroup bloodGroup, BloodComponentType componentType,
                        int unitsRequested, UrgencyLevel urgencyLevel, RequestStatus status,
                        String deliveryAddress, String prescriptionNotes, String rejectionReason,
                        BloodDelivery delivery, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.requester = requester;
        this.hospital = hospital;
        this.patientName = patientName;
        this.admittedHospitalName = admittedHospitalName;
        this.bloodGroup = bloodGroup;
        this.componentType = componentType != null ? componentType : BloodComponentType.WHOLE_BLOOD;
        this.unitsRequested = unitsRequested;
        this.urgencyLevel = urgencyLevel != null ? urgencyLevel : UrgencyLevel.NORMAL;
        this.status = status != null ? status : RequestStatus.PENDING;
        this.deliveryAddress = deliveryAddress;
        this.prescriptionNotes = prescriptionNotes;
        this.rejectionReason = rejectionReason;
        this.delivery = delivery;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getRequester() { return requester; }
    public void setRequester(User requester) { this.requester = requester; }

    public HospitalProfile getHospital() { return hospital; }
    public void setHospital(HospitalProfile hospital) { this.hospital = hospital; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getAdmittedHospitalName() { return admittedHospitalName; }
    public void setAdmittedHospitalName(String admittedHospitalName) { this.admittedHospitalName = admittedHospitalName; }

    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }

    public BloodComponentType getComponentType() { return componentType; }
    public void setComponentType(BloodComponentType componentType) { this.componentType = componentType; }

    public int getUnitsRequested() { return unitsRequested; }
    public void setUnitsRequested(int unitsRequested) { this.unitsRequested = unitsRequested; }

    public UrgencyLevel getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPrescriptionNotes() { return prescriptionNotes; }
    public void setPrescriptionNotes(String prescriptionNotes) { this.prescriptionNotes = prescriptionNotes; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public BloodDelivery getDelivery() { return delivery; }
    public void setDelivery(BloodDelivery delivery) { this.delivery = delivery; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static BloodRequestBuilder builder() { return new BloodRequestBuilder(); }

    public static class BloodRequestBuilder {
        private Long id;
        private User requester;
        private HospitalProfile hospital;
        private String patientName;
        private String admittedHospitalName;
        private BloodGroup bloodGroup;
        private BloodComponentType componentType = BloodComponentType.WHOLE_BLOOD;
        private int unitsRequested;
        private UrgencyLevel urgencyLevel = UrgencyLevel.NORMAL;
        private RequestStatus status = RequestStatus.PENDING;
        private String deliveryAddress;
        private String prescriptionNotes;
        private String rejectionReason;
        private BloodDelivery delivery;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public BloodRequestBuilder id(Long id) { this.id = id; return this; }
        public BloodRequestBuilder requester(User requester) { this.requester = requester; return this; }
        public BloodRequestBuilder hospital(HospitalProfile hospital) { this.hospital = hospital; return this; }
        public BloodRequestBuilder patientName(String patientName) { this.patientName = patientName; return this; }
        public BloodRequestBuilder admittedHospitalName(String admittedHospitalName) { this.admittedHospitalName = admittedHospitalName; return this; }
        public BloodRequestBuilder bloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public BloodRequestBuilder componentType(BloodComponentType componentType) { this.componentType = componentType; return this; }
        public BloodRequestBuilder unitsRequested(int unitsRequested) { this.unitsRequested = unitsRequested; return this; }
        public BloodRequestBuilder urgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; return this; }
        public BloodRequestBuilder status(RequestStatus status) { this.status = status; return this; }
        public BloodRequestBuilder deliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; return this; }
        public BloodRequestBuilder prescriptionNotes(String prescriptionNotes) { this.prescriptionNotes = prescriptionNotes; return this; }
        public BloodRequestBuilder rejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; return this; }
        public BloodRequestBuilder delivery(BloodDelivery delivery) { this.delivery = delivery; return this; }
        public BloodRequestBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public BloodRequestBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public BloodRequest build() {
            return new BloodRequest(id, requester, hospital, patientName, admittedHospitalName, bloodGroup, componentType, unitsRequested, urgencyLevel, status, deliveryAddress, prescriptionNotes, rejectionReason, delivery, createdAt, updatedAt);
        }
    }
}
