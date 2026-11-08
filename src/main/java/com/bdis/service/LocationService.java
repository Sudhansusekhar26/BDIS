package com.bdis.service;

import com.bdis.dto.LocationDto;
import com.bdis.entity.LocationDistrict;
import com.bdis.entity.LocationState;

import java.util.List;

public interface LocationService {

    List<LocationState> getAllStates();

    List<LocationDistrict> getDistrictsByStateId(Long stateId);

    LocationState getStateById(Long id);

    LocationDistrict getDistrictById(Long id);

    List<LocationDto> getAllStatesDto();

    List<LocationDto> getDistrictsDtoByStateId(Long stateId);
}
