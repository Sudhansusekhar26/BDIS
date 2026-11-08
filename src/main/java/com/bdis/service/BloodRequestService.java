package com.bdis.service;

import com.bdis.dto.BloodRequestCreateDto;
import com.bdis.dto.BloodRequestResponseDto;
import com.bdis.dto.DeliveryUpdateDto;
import com.bdis.entity.BloodRequest;
import com.bdis.enums.RequestStatus;

import java.util.List;

public interface BloodRequestService {

    BloodRequest createRequest(Long userId, BloodRequestCreateDto dto);

    BloodRequest getRequestById(Long id);

    BloodRequestResponseDto getRequestDtoById(Long id);

    List<BloodRequestResponseDto> getRequestsByRequester(Long userId);

    List<BloodRequestResponseDto> getRequestsByHospital(Long hospitalId);

    List<BloodRequestResponseDto> getRequestsByHospitalAndStatus(Long hospitalId, RequestStatus status);

    List<BloodRequestResponseDto> getAllRequests();

    BloodRequest acceptRequest(Long requestId, String performedBy);

    BloodRequest rejectRequest(Long requestId, String reason, String performedBy);

    BloodRequest cancelRequest(Long requestId, Long userId);

    BloodRequest updateDeliveryInfo(Long requestId, DeliveryUpdateDto dto, String performedBy);

    BloodRequest updateStatus(Long requestId, RequestStatus newStatus, String performedBy);
}
