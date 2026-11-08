package com.bdis.repository;

import com.bdis.entity.LocationDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationDistrictRepository extends JpaRepository<LocationDistrict, Long> {

    List<LocationDistrict> findByState_IdOrderByNameAsc(Long stateId);

    Optional<LocationDistrict> findByNameIgnoreCaseAndState_Id(String name, Long stateId);
}
