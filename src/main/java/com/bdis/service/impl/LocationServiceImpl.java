package com.bdis.service.impl;

import com.bdis.dto.LocationDto;
import com.bdis.entity.LocationDistrict;
import com.bdis.entity.LocationState;
import com.bdis.exception.ResourceNotFoundException;
import com.bdis.repository.LocationDistrictRepository;
import com.bdis.repository.LocationStateRepository;
import com.bdis.service.LocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService {

    private final LocationStateRepository stateRepository;
    private final LocationDistrictRepository districtRepository;

    public LocationServiceImpl(LocationStateRepository stateRepository, LocationDistrictRepository districtRepository) {
        this.stateRepository = stateRepository;
        this.districtRepository = districtRepository;
    }

    @Override
    public List<LocationState> getAllStates() {
        return stateRepository.findAllByOrderByNameAsc();
    }

    @Override
    public List<LocationDistrict> getDistrictsByStateId(Long stateId) {
        return districtRepository.findByState_IdOrderByNameAsc(stateId);
    }

    @Override
    public LocationState getStateById(Long id) {
        return stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State not found with ID: " + id));
    }

    @Override
    public LocationDistrict getDistrictById(Long id) {
        return districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("District not found with ID: " + id));
    }

    @Override
    public List<LocationDto> getAllStatesDto() {
        return getAllStates().stream()
                .map(LocationDto::fromState)
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationDto> getDistrictsDtoByStateId(Long stateId) {
        return getDistrictsByStateId(stateId).stream()
                .map(LocationDto::fromDistrict)
                .collect(Collectors.toList());
    }
}
