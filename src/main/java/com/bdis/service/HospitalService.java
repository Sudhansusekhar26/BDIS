package com.bdis.service;

import com.bdis.dto.HospitalRegistrationDto;
import com.bdis.entity.HospitalProfile;

import java.util.List;

public interface HospitalService {

    HospitalProfile registerHospital(HospitalRegistrationDto dto);

    HospitalProfile getHospitalById(Long id);

    HospitalProfile getHospitalByUserId(Long userId);

    HospitalProfile getHospitalByUserEmail(String email);

    List<HospitalProfile> getAllHospitals();

    List<HospitalProfile> getVerifiedHospitals();

    List<HospitalProfile> getPendingHospitals();

    List<HospitalProfile> getVerifiedHospitalsByLocation(Long stateId, Long districtId);

    HospitalProfile verifyHospital(Long hospitalId, boolean approved);
}
