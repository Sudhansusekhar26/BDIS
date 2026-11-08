package com.bdis.entity;

import com.bdis.enums.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_deliveries")
public class BloodDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false, unique = true)
    @JsonIgnore
    private BloodRequest request;

    @Column(name = "delivery_partner_name", nullable = false, length = 150)
    private String deliveryPartnerName;

    @Column(name = "delivery_contact", nullable = false, length = 50)
    private String deliveryContact;

    @Column(name = "vehicle_number", length = 50)
    private String vehicleNumber;

    @Column(name = "tracking_notes", columnDefinition = "TEXT")
    private String trackingNotes;

    @Column(name = "dispatched_at")
    private LocalDateTime dispatchedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false, length = 30)
    private DeliveryStatus currentStatus = DeliveryStatus.ASSIGNED;

    public BloodDelivery() {}

    public BloodDelivery(Long id, BloodRequest request, String deliveryPartnerName, String deliveryContact,
                         String vehicleNumber, String trackingNotes, LocalDateTime dispatchedAt,
                         LocalDateTime deliveredAt, DeliveryStatus currentStatus) {
        this.id = id;
        this.request = request;
        this.deliveryPartnerName = deliveryPartnerName;
        this.deliveryContact = deliveryContact;
        this.vehicleNumber = vehicleNumber;
        this.trackingNotes = trackingNotes;
        this.dispatchedAt = dispatchedAt;
        this.deliveredAt = deliveredAt;
        this.currentStatus = currentStatus != null ? currentStatus : DeliveryStatus.ASSIGNED;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BloodRequest getRequest() { return request; }
    public void setRequest(BloodRequest request) { this.request = request; }

    public String getDeliveryPartnerName() { return deliveryPartnerName; }
    public void setDeliveryPartnerName(String deliveryPartnerName) { this.deliveryPartnerName = deliveryPartnerName; }

    public String getDeliveryContact() { return deliveryContact; }
    public void setDeliveryContact(String deliveryContact) { this.deliveryContact = deliveryContact; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getTrackingNotes() { return trackingNotes; }
    public void setTrackingNotes(String trackingNotes) { this.trackingNotes = trackingNotes; }

    public LocalDateTime getDispatchedAt() { return dispatchedAt; }
    public void setDispatchedAt(LocalDateTime dispatchedAt) { this.dispatchedAt = dispatchedAt; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }

    public DeliveryStatus getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(DeliveryStatus currentStatus) { this.currentStatus = currentStatus; }

    public static BloodDeliveryBuilder builder() { return new BloodDeliveryBuilder(); }

    public static class BloodDeliveryBuilder {
        private Long id;
        private BloodRequest request;
        private String deliveryPartnerName;
        private String deliveryContact;
        private String vehicleNumber;
        private String trackingNotes;
        private LocalDateTime dispatchedAt;
        private LocalDateTime deliveredAt;
        private DeliveryStatus currentStatus = DeliveryStatus.ASSIGNED;

        public BloodDeliveryBuilder id(Long id) { this.id = id; return this; }
        public BloodDeliveryBuilder request(BloodRequest request) { this.request = request; return this; }
        public BloodDeliveryBuilder deliveryPartnerName(String deliveryPartnerName) { this.deliveryPartnerName = deliveryPartnerName; return this; }
        public BloodDeliveryBuilder deliveryContact(String deliveryContact) { this.deliveryContact = deliveryContact; return this; }
        public BloodDeliveryBuilder vehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; return this; }
        public BloodDeliveryBuilder trackingNotes(String trackingNotes) { this.trackingNotes = trackingNotes; return this; }
        public BloodDeliveryBuilder dispatchedAt(LocalDateTime dispatchedAt) { this.dispatchedAt = dispatchedAt; return this; }
        public BloodDeliveryBuilder deliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; return this; }
        public BloodDeliveryBuilder currentStatus(DeliveryStatus currentStatus) { this.currentStatus = currentStatus; return this; }

        public BloodDelivery build() {
            return new BloodDelivery(id, request, deliveryPartnerName, deliveryContact, vehicleNumber, trackingNotes, dispatchedAt, deliveredAt, currentStatus);
        }
    }
}
