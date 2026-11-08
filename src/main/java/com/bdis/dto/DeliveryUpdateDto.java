package com.bdis.dto;

import com.bdis.enums.DeliveryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DeliveryUpdateDto {

    @NotBlank(message = "Delivery partner name is required")
    private String deliveryPartnerName;

    @NotBlank(message = "Delivery contact phone is required")
    private String deliveryContact;

    private String vehicleNumber;
    private String trackingNotes;

    @NotNull(message = "Delivery status is required")
    private DeliveryStatus currentStatus;

    public DeliveryUpdateDto() {}

    public DeliveryUpdateDto(String deliveryPartnerName, String deliveryContact, String vehicleNumber, String trackingNotes, DeliveryStatus currentStatus) {
        this.deliveryPartnerName = deliveryPartnerName;
        this.deliveryContact = deliveryContact;
        this.vehicleNumber = vehicleNumber;
        this.trackingNotes = trackingNotes;
        this.currentStatus = currentStatus;
    }

    public String getDeliveryPartnerName() { return deliveryPartnerName; }
    public void setDeliveryPartnerName(String deliveryPartnerName) { this.deliveryPartnerName = deliveryPartnerName; }

    public String getDeliveryContact() { return deliveryContact; }
    public void setDeliveryContact(String deliveryContact) { this.deliveryContact = deliveryContact; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getTrackingNotes() { return trackingNotes; }
    public void setTrackingNotes(String trackingNotes) { this.trackingNotes = trackingNotes; }

    public DeliveryStatus getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(DeliveryStatus currentStatus) { this.currentStatus = currentStatus; }

    public static DeliveryUpdateDtoBuilder builder() { return new DeliveryUpdateDtoBuilder(); }

    public static class DeliveryUpdateDtoBuilder {
        private String deliveryPartnerName;
        private String deliveryContact;
        private String vehicleNumber;
        private String trackingNotes;
        private DeliveryStatus currentStatus;

        public DeliveryUpdateDtoBuilder deliveryPartnerName(String deliveryPartnerName) { this.deliveryPartnerName = deliveryPartnerName; return this; }
        public DeliveryUpdateDtoBuilder deliveryContact(String deliveryContact) { this.deliveryContact = deliveryContact; return this; }
        public DeliveryUpdateDtoBuilder vehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; return this; }
        public DeliveryUpdateDtoBuilder trackingNotes(String trackingNotes) { this.trackingNotes = trackingNotes; return this; }
        public DeliveryUpdateDtoBuilder currentStatus(DeliveryStatus currentStatus) { this.currentStatus = currentStatus; return this; }

        public DeliveryUpdateDto build() {
            return new DeliveryUpdateDto(deliveryPartnerName, deliveryContact, vehicleNumber, trackingNotes, currentStatus);
        }
    }
}
