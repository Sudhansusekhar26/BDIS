package com.bdis.service;

import com.bdis.dto.DeliveryUpdateDto;
import com.bdis.entity.BloodDelivery;
import com.bdis.entity.BloodRequest;

public interface BloodDeliveryService {

    BloodDelivery createInitialDelivery(BloodRequest request);

    BloodDelivery updateDeliveryDetails(Long requestId, DeliveryUpdateDto dto);

    BloodDelivery getDeliveryByRequestId(Long requestId);
}
