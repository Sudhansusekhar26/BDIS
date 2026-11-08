package com.bdis.controller;

import com.bdis.dto.BloodInventoryDto;
import com.bdis.dto.BloodRequestResponseDto;
import com.bdis.entity.LocationState;
import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import com.bdis.service.BloodInventoryService;
import com.bdis.service.BloodRequestService;
import com.bdis.service.HospitalService;
import com.bdis.service.LocationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final LocationService locationService;
    private final BloodInventoryService inventoryService;
    private final HospitalService hospitalService;
    private final BloodRequestService requestService;

    public HomeController(LocationService locationService,
                          BloodInventoryService inventoryService,
                          HospitalService hospitalService,
                          BloodRequestService requestService) {
        this.locationService = locationService;
        this.inventoryService = inventoryService;
        this.hospitalService = hospitalService;
        this.requestService = requestService;
    }

    @GetMapping({"/", "/index"})
    public String home(
            @RequestParam(value = "bloodGroup", required = false) String bloodGroupStr,
            @RequestParam(value = "componentType", required = false) String componentTypeStr,
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "districtId", required = false) Long districtId,
            Authentication authentication,
            Model model) {

        // Auto-redirect authenticated users to their specific role's home page
        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedAuthority auth : authentication.getAuthorities()) {
                if ("ROLE_ADMIN".equals(auth.getAuthority())) {
                    return "redirect:/admin/dashboard";
                } else if ("ROLE_HOSPITAL".equals(auth.getAuthority())) {
                    return "redirect:/hospital/dashboard";
                } else if ("ROLE_USER".equals(auth.getAuthority())) {
                    return "redirect:/user/dashboard";
                }
            }
        }

        // Guest / Public landing page with live stock search
        List<LocationState> states = locationService.getAllStates();
        model.addAttribute("states", states);
        model.addAttribute("bloodGroups", BloodGroup.values());
        model.addAttribute("componentTypes", BloodComponentType.values());

        BloodGroup bloodGroup = BloodGroup.fromLabel(bloodGroupStr);
        if (bloodGroup == null && bloodGroupStr != null && !bloodGroupStr.isEmpty()) {
            try {
                bloodGroup = BloodGroup.valueOf(bloodGroupStr.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        BloodComponentType componentType = null;
        if (componentTypeStr != null && !componentTypeStr.isEmpty()) {
            try {
                componentType = BloodComponentType.valueOf(componentTypeStr.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        List<BloodInventoryDto> inventoryResults = inventoryService.searchAvailableInventory(
                bloodGroup, componentType, stateId, districtId);

        model.addAttribute("inventoryResults", inventoryResults);
        model.addAttribute("selectedStateId", stateId);
        model.addAttribute("selectedDistrictId", districtId);
        model.addAttribute("selectedBloodGroup", bloodGroupStr);
        model.addAttribute("selectedComponentType", componentTypeStr);
        model.addAttribute("totalHospitals", hospitalService.getVerifiedHospitals().size());

        return "index";
    }

    @GetMapping("/track/{id}")
    public String trackRequestPublic(@PathVariable Long id, Model model) {
        try {
            BloodRequestResponseDto request = requestService.getRequestDtoById(id);
            model.addAttribute("bloodRequest", request);
            return "track-request";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Blood request not found for tracking ID: " + id);
            return "error/404";
        }
    }
}
