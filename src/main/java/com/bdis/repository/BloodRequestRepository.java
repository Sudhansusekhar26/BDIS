package com.bdis.repository;

import com.bdis.entity.BloodRequest;
import com.bdis.enums.RequestStatus;
import com.bdis.enums.UrgencyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {

    List<BloodRequest> findByRequester_IdOrderByCreatedAtDesc(Long requesterId);

    List<BloodRequest> findByHospital_IdOrderByCreatedAtDesc(Long hospitalId);

    List<BloodRequest> findByHospital_IdAndStatusOrderByCreatedAtDesc(Long hospitalId, RequestStatus status);

    List<BloodRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);

    List<BloodRequest> findAllByOrderByCreatedAtDesc();

    long countByStatus(RequestStatus status);

    long countByHospital_IdAndStatus(Long hospitalId, RequestStatus status);

    long countByUrgencyLevelAndStatus(UrgencyLevel urgencyLevel, RequestStatus status);

    @Query("SELECT r FROM BloodRequest r WHERE r.hospital.id = :hospitalId AND r.status IN :statuses ORDER BY r.createdAt DESC")
    List<BloodRequest> findActiveHospitalRequests(@Param("hospitalId") Long hospitalId, @Param("statuses") List<RequestStatus> statuses);

    @Query("SELECT r FROM BloodRequest r WHERE r.urgencyLevel = :urgency AND r.status = 'PENDING'")
    List<BloodRequest> findPendingEmergencyRequests(@Param("urgency") UrgencyLevel urgency);
}
