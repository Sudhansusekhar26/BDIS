package com.bdis.repository;

import com.bdis.entity.DonorPledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonorPledgeRepository extends JpaRepository<DonorPledge, Long> {

    List<DonorPledge> findByDonor_IdOrderByCreatedAtDesc(Long donorId);

    List<DonorPledge> findByHospital_IdOrderByCreatedAtDesc(Long hospitalId);

    long countByDonor_Id(Long donorId);
}
