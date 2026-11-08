package com.bdis.repository;

import com.bdis.entity.BloodInventory;
import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {

    List<BloodInventory> findByHospital_Id(Long hospitalId);

    List<BloodInventory> findByHospital_IdOrderByBloodGroupAsc(Long hospitalId);

    Optional<BloodInventory> findByHospital_IdAndBloodGroupAndComponentTypeAndBatchNumber(
            Long hospitalId,
            BloodGroup bloodGroup,
            BloodComponentType componentType,
            String batchNumber
    );

    List<BloodInventory> findByHospital_IdAndBloodGroupAndComponentType(
            Long hospitalId,
            BloodGroup bloodGroup,
            BloodComponentType componentType
    );

    @Query("SELECT bi FROM BloodInventory bi WHERE bi.hospital.isVerified = true " +
           "AND (:bloodGroup IS NULL OR bi.bloodGroup = :bloodGroup) " +
           "AND (:componentType IS NULL OR bi.componentType = :componentType) " +
           "AND (:stateId IS NULL OR bi.hospital.state.id = :stateId) " +
           "AND (:districtId IS NULL OR bi.hospital.district.id = :districtId) " +
           "AND bi.unitsAvailable > 0 " +
           "ORDER BY bi.unitsAvailable DESC")
    List<BloodInventory> searchAvailableInventory(
            @Param("bloodGroup") BloodGroup bloodGroup,
            @Param("componentType") BloodComponentType componentType,
            @Param("stateId") Long stateId,
            @Param("districtId") Long districtId
    );

    @Query("SELECT COALESCE(SUM(bi.unitsAvailable), 0) FROM BloodInventory bi WHERE bi.hospital.isVerified = true")
    long getTotalAvailableUnits();

    @Query("SELECT bi.bloodGroup, COALESCE(SUM(bi.unitsAvailable), 0) FROM BloodInventory bi WHERE bi.hospital.isVerified = true GROUP BY bi.bloodGroup")
    List<Object[]> getUnitsCountByBloodGroup();

    @Query("SELECT bi.componentType, COALESCE(SUM(bi.unitsAvailable), 0) FROM BloodInventory bi WHERE bi.hospital.isVerified = true GROUP BY bi.componentType")
    List<Object[]> getUnitsCountByComponentType();

    @Query("SELECT bi FROM BloodInventory bi WHERE bi.unitsAvailable > 0 AND bi.unitsAvailable <= :threshold")
    List<BloodInventory> findLowStockItems(@Param("threshold") int threshold);

    @Query("SELECT bi FROM BloodInventory bi WHERE bi.expiryDate IS NOT NULL AND bi.expiryDate <= :date AND bi.unitsAvailable > 0")
    List<BloodInventory> findExpiringStock(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(bi.unitsAvailable), 0) FROM BloodInventory bi " +
           "WHERE bi.hospital.id = :hospitalId AND bi.bloodGroup = :bloodGroup AND bi.componentType = :componentType")
    int sumAvailableUnitsForHospital(
            @Param("hospitalId") Long hospitalId,
            @Param("bloodGroup") BloodGroup bloodGroup,
            @Param("componentType") BloodComponentType componentType
    );
}
