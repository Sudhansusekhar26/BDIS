package com.bdis.repository;

import com.bdis.entity.BloodDelivery;
import com.bdis.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BloodDeliveryRepository extends JpaRepository<BloodDelivery, Long> {

    Optional<BloodDelivery> findByRequest_Id(Long requestId);

    List<BloodDelivery> findByCurrentStatus(DeliveryStatus status);

    long countByCurrentStatus(DeliveryStatus status);
}
