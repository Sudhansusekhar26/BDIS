package com.bdis.controller.api;

import com.bdis.dto.ApiResponse;
import com.bdis.dto.LocationDto;
import com.bdis.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationApiController {

    private final LocationService locationService;

    public LocationApiController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/states")
    public ResponseEntity<ApiResponse<List<LocationDto>>> getAllStates() {
        List<LocationDto> states = locationService.getAllStatesDto();
        return ResponseEntity.ok(ApiResponse.ok("States fetched successfully", states));
    }

    @GetMapping("/districts")
    public ResponseEntity<ApiResponse<List<LocationDto>>> getDistrictsByState(@RequestParam("stateId") Long stateId) {
        List<LocationDto> districts = locationService.getDistrictsDtoByStateId(stateId);
        return ResponseEntity.ok(ApiResponse.ok("Districts fetched successfully", districts));
    }
}
