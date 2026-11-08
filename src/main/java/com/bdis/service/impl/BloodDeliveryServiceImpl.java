package com.bdis.service.impl;

import com.bdis.dto.DeliveryUpdateDto;
import com.bdis.entity.BloodDelivery;
import com.bdis.entity.BloodRequest;
import com.bdis.enums.DeliveryStatus;
import com.bdis.exception.ResourceNotFoundException;
import com.bdis.repository.BloodDeliveryRepository;
import com.bdis.service.BloodDeliveryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BloodDeliveryServiceImpl implements BloodDeliveryService {

    private final BloodDeliveryRepository deliveryRepository;

    public BloodDeliveryServiceImpl(BloodDeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    @Transactional
    public BloodDelivery createInitialDelivery(BloodRequest request) {
        BloodDelivery delivery = BloodDelivery.builder()
                .request(request)
                .deliveryPartnerName("Hospital Dedicated Ambulance / Emergency Transit")
                .deliveryContact(request.getHospital().getEmergencyHelpline())
                .currentStatus(DeliveryStatus.ASSIGNED)
                .trackingNotes("Request approved. Blood unit verified and prepared for transit.")
                .build();
        return deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public BloodDelivery updateDeliveryDetails(Long requestId, DeliveryUpdateDto dto) {
        BloodDelivery delivery = deliveryRepository.findByRequest_Id(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery record not found for request ID: " + requestId));

        delivery.setDeliveryPartnerName(dto.getDeliveryPartnerName().trim());
        delivery.setDeliveryContact(dto.getDeliveryContact().trim());
        delivery.setVehicleNumber(dto.getVehicleNumber() != null ? dto.getVehicleNumber().trim() : null);
        delivery.setTrackingNotes(dto.getTrackingNotes() != null ? dto.getTrackingNotes().trim() : null);
        delivery.setCurrentStatus(dto.getCurrentStatus());

        if (dto.getCurrentStatus() == DeliveryStatus.IN_TRANSIT && delivery.getDispatchedAt() == null) {
            delivery.setDispatchedAt(LocalDateTime.now());
        } else if (dto.getCurrentStatus() == DeliveryStatus.DELIVERED) {
            if (delivery.getDispatchedAt() == null) {
                delivery.setDispatchedAt(LocalDateTime.now());
            }
            delivery.setDeliveredAt(LocalDateTime.now());
        }

        return deliveryRepository.save(delivery);
    }

    @Override
    @Transactional(readOnly = true)
    public BloodDelivery getDeliveryByRequestId(Long requestId) {
        return deliveryRepository.findByRequest_Id(requestId)
                .orElse(null);
    }
}
