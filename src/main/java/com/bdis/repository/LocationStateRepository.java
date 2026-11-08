package com.bdis.repository;

import com.bdis.entity.LocationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationStateRepository extends JpaRepository<LocationState, Long> {

    List<LocationState> findAllByOrderByNameAsc();

    Optional<LocationState> findByNameIgnoreCase(String name);

    Optional<LocationState> findByCodeIgnoreCase(String code);
}
