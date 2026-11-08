package com.bdis.repository;

import com.bdis.entity.HospitalProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalProfileRepository extends JpaRepository<HospitalProfile, Long> {

    Optional<HospitalProfile> findByUser_Id(Long userId);

    Optional<HospitalProfile> findByUser_Email(String email);

    Optional<HospitalProfile> findByLicenseNumber(String licenseNumber);

    boolean existsByLicenseNumber(String licenseNumber);

    List<HospitalProfile> findByIsVerified(boolean isVerified);

    long countByIsVerified(boolean isVerified);

    @Query("SELECT h FROM HospitalProfile h WHERE h.isVerified = true AND (:stateId IS NULL OR h.state.id = :stateId) AND (:districtId IS NULL OR h.district.id = :districtId)")
    List<HospitalProfile> findVerifiedByLocation(@Param("stateId") Long stateId, @Param("districtId") Long districtId);
}
