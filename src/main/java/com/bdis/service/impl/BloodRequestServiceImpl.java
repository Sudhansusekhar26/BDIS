package com.bdis.service.impl;

import com.bdis.dto.BloodRequestCreateDto;
import com.bdis.dto.BloodRequestResponseDto;
import com.bdis.dto.DeliveryUpdateDto;
import com.bdis.entity.BloodDelivery;
import com.bdis.entity.BloodRequest;
import com.bdis.entity.HospitalProfile;
import com.bdis.entity.User;
import com.bdis.enums.DeliveryStatus;
import com.bdis.enums.RequestStatus;
import com.bdis.exception.InvalidOperationException;
import com.bdis.exception.ResourceNotFoundException;
import com.bdis.repository.BloodRequestRepository;
import com.bdis.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BloodRequestServiceImpl implements BloodRequestService {

    private static final Logger log = LoggerFactory.getLogger(BloodRequestServiceImpl.class);

    private final BloodRequestRepository requestRepository;
    private final UserService userService;
    private final HospitalService hospitalService;
    private final BloodInventoryService inventoryService;
    private final BloodDeliveryService deliveryService;
    private final AuditLogService auditLogService;

    public BloodRequestServiceImpl(BloodRequestRepository requestRepository,
                                   UserService userService,
                                   HospitalService hospitalService,
                                   BloodInventoryService inventoryService,
                                   BloodDeliveryService deliveryService,
                                   AuditLogService auditLogService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.hospitalService = hospitalService;
        this.inventoryService = inventoryService;
        this.deliveryService = deliveryService;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public BloodRequest createRequest(Long userId, BloodRequestCreateDto dto) {
        User user = userService.getUserById(userId);
        HospitalProfile hospital = hospitalService.getHospitalById(dto.getHospitalId());

        if (!hospital.isVerified()) {
            throw new InvalidOperationException("Selected hospital is not yet verified to fulfill blood requests.");
        }

        BloodRequest request = BloodRequest.builder()
                .requester(user)
                .hospital(hospital)
                .patientName(dto.getPatientName().trim())
                .admittedHospitalName(dto.getAdmittedHospitalName().trim())
                .bloodGroup(dto.getBloodGroup())
                .componentType(dto.getComponentType())
                .unitsRequested(dto.getUnitsRequested())
                .urgencyLevel(dto.getUrgencyLevel())
                .status(RequestStatus.PENDING)
                .deliveryAddress(dto.getDeliveryAddress().trim())
                .prescriptionNotes(dto.getPrescriptionNotes() != null ? dto.getPrescriptionNotes().trim() : null)
                .build();

        BloodRequest saved = requestRepository.save(request);
        auditLogService.log("BLOOD_REQUEST_CREATED", user.getEmail(), "BloodRequest", String.valueOf(saved.getId()),
                "Created blood request for " + saved.getUnitsRequested() + " units of " + saved.getBloodGroup().getLabel() + " (Urgency: " + saved.getUrgencyLevel().getLabel() + ")");
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public BloodRequest getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blood request not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public BloodRequestResponseDto getRequestDtoById(Long id) {
        return mapToDto(getRequestById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodRequestResponseDto> getRequestsByRequester(Long userId) {
        return requestRepository.findByRequester_IdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodRequestResponseDto> getRequestsByHospital(Long hospitalId) {
        return requestRepository.findByHospital_IdOrderByCreatedAtDesc(hospitalId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodRequestResponseDto> getRequestsByHospitalAndStatus(Long hospitalId, RequestStatus status) {
        return requestRepository.findByHospital_IdAndStatusOrderByCreatedAtDesc(hospitalId, status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodRequestResponseDto> getAllRequests() {
        return requestRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BloodRequest acceptRequest(Long requestId, String performedBy) {
        BloodRequest request = getRequestById(requestId);

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new InvalidOperationException("Only PENDING requests can be accepted. Current status: " + request.getStatus());
        }

        // Deduct stock atomically from hospital inventory
        inventoryService.deductStockForRequest(
                request.getHospital().getId(),
                request.getBloodGroup(),
                request.getComponentType(),
                request.getUnitsRequested()
        );

        request.setStatus(RequestStatus.ACCEPTED);

        // Initialize delivery pipeline & link bidirectionally
        BloodDelivery delivery = deliveryService.createInitialDelivery(request);
        request.setDelivery(delivery);

        BloodRequest updated = requestRepository.save(request);

        auditLogService.log("BLOOD_REQUEST_ACCEPTED", performedBy, "BloodRequest", String.valueOf(requestId),
                "Accepted request and allocated " + request.getUnitsRequested() + " units of " + request.getBloodGroup().getLabel());
        return updated;
    }

    @Override
    @Transactional
    public BloodRequest rejectRequest(Long requestId, String reason, String performedBy) {
        BloodRequest request = getRequestById(requestId);

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new InvalidOperationException("Only PENDING requests can be rejected. Current status: " + request.getStatus());
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason != null ? reason.trim() : "Rejected by hospital administration");
        BloodRequest updated = requestRepository.save(request);

        auditLogService.log("BLOOD_REQUEST_REJECTED", performedBy, "BloodRequest", String.valueOf(requestId),
                "Rejected request. Reason: " + request.getRejectionReason());
        return updated;
    }

    @Override
    @Transactional
    public BloodRequest cancelRequest(Long requestId, Long userId) {
        BloodRequest request = getRequestById(requestId);

        if (!request.getRequester().getId().equals(userId)) {
            throw new InvalidOperationException("You do not have permission to cancel this request.");
        }

        if (request.getStatus() == RequestStatus.DELIVERED || request.getStatus() == RequestStatus.DISPATCHED) {
            throw new InvalidOperationException("Cannot cancel a request that is already dispatched or delivered.");
        }

        // If previously accepted, restore deducted stock
        if (request.getStatus() == RequestStatus.ACCEPTED) {
            inventoryService.restoreStockForCancelledRequest(
                    request.getHospital().getId(),
                    request.getBloodGroup(),
                    request.getComponentType(),
                    request.getUnitsRequested()
            );
        }

        request.setStatus(RequestStatus.CANCELLED);
        BloodRequest updated = requestRepository.save(request);
        auditLogService.log("BLOOD_REQUEST_CANCELLED", request.getRequester().getEmail(), "BloodRequest", String.valueOf(requestId), "Requester cancelled blood requisition");
        return updated;
    }

    @Override
    @Transactional
    public BloodRequest updateDeliveryInfo(Long requestId, DeliveryUpdateDto dto, String performedBy) {
        BloodRequest request = getRequestById(requestId);
        BloodDelivery delivery = deliveryService.updateDeliveryDetails(requestId, dto);
        request.setDelivery(delivery);

        if (dto.getCurrentStatus() == DeliveryStatus.IN_TRANSIT) {
            request.setStatus(RequestStatus.DISPATCHED);
        } else if (dto.getCurrentStatus() == DeliveryStatus.DELIVERED) {
            request.setStatus(RequestStatus.DELIVERED);
        }

        BloodRequest updated = requestRepository.save(request);
        auditLogService.log("DELIVERY_UPDATE", performedBy, "BloodDelivery", String.valueOf(delivery.getId()),
                "Transit update: " + dto.getCurrentStatus().getLabel() + " (Rider: " + dto.getDeliveryPartnerName() + ")");
        return updated;
    }

    @Override
    @Transactional
    public BloodRequest updateStatus(Long requestId, RequestStatus newStatus, String performedBy) {
        BloodRequest request = getRequestById(requestId);
        request.setStatus(newStatus);

        BloodDelivery delivery = deliveryService.getDeliveryByRequestId(requestId);
        if (delivery != null) {
            if (newStatus == RequestStatus.DISPATCHED) {
                delivery.setCurrentStatus(DeliveryStatus.IN_TRANSIT);
                if (delivery.getDispatchedAt() == null) {
                    delivery.setDispatchedAt(LocalDateTime.now());
                }
            } else if (newStatus == RequestStatus.DELIVERED) {
                delivery.setCurrentStatus(DeliveryStatus.DELIVERED);
                if (delivery.getDispatchedAt() == null) {
                    delivery.setDispatchedAt(LocalDateTime.now());
                }
                delivery.setDeliveredAt(LocalDateTime.now());
            }
            request.setDelivery(delivery);
        }

        BloodRequest updated = requestRepository.save(request);
        auditLogService.log("REQUEST_STATUS_CHANGE", performedBy, "BloodRequest", String.valueOf(requestId), "Status transitioned to " + newStatus.getLabel());
        return updated;
    }

    private BloodRequestResponseDto mapToDto(BloodRequest r) {
        BloodDelivery del = r.getDelivery();

        return BloodRequestResponseDto.builder()
                .id(r.getId())
                .requesterId(r.getRequester() != null ? r.getRequester().getId() : null)
                .requesterName(r.getRequester() != null ? r.getRequester().getName() : "")
                .requesterPhone(r.getRequester() != null ? r.getRequester().getPhone() : "")
                .requesterEmail(r.getRequester() != null ? r.getRequester().getEmail() : "")
                .hospitalId(r.getHospital() != null ? r.getHospital().getId() : null)
                .hospitalName(r.getHospital() != null ? r.getHospital().getHospitalName() : "")
                .hospitalHelpline(r.getHospital() != null ? r.getHospital().getEmergencyHelpline() : "")
                .hospitalAddress(r.getHospital() != null ? r.getHospital().getAddress() : "")
                .patientName(r.getPatientName())
                .admittedHospitalName(r.getAdmittedHospitalName())
                .bloodGroup(r.getBloodGroup())
                .bloodGroupLabel(r.getBloodGroup() != null ? r.getBloodGroup().getLabel() : "")
                .componentType(r.getComponentType())
                .componentName(r.getComponentType() != null ? r.getComponentType().getDisplayName() : "")
                .unitsRequested(r.getUnitsRequested())
                .urgencyLevel(r.getUrgencyLevel())
                .status(r.getStatus())
                .statusBadgeClass(r.getStatus() != null ? r.getStatus().getBadgeClass() : "bg-secondary")
                .deliveryAddress(r.getDeliveryAddress())
                .prescriptionNotes(r.getPrescriptionNotes())
                .rejectionReason(r.getRejectionReason())
                .deliveryId(del != null ? del.getId() : null)
                .deliveryPartnerName(del != null ? del.getDeliveryPartnerName() : null)
                .deliveryContact(del != null ? del.getDeliveryContact() : null)
                .vehicleNumber(del != null ? del.getVehicleNumber() : null)
                .trackingNotes(del != null ? del.getTrackingNotes() : null)
                .deliveryStatus(del != null ? del.getCurrentStatus() : null)
                .dispatchedAt(del != null ? del.getDispatchedAt() : null)
                .deliveredAt(del != null ? del.getDeliveredAt() : null)
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
