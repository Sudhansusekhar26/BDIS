package com.bdis.controller.api;

import com.bdis.dto.ApiResponse;
import com.bdis.dto.BloodInventoryDto;
import com.bdis.dto.StockUpdateDto;
import com.bdis.entity.BloodInventory;
import com.bdis.entity.HospitalProfile;
import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import com.bdis.service.BloodInventoryService;
import com.bdis.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryApiController {

    private final BloodInventoryService inventoryService;
    private final HospitalService hospitalService;

    public InventoryApiController(BloodInventoryService inventoryService, HospitalService hospitalService) {
        this.inventoryService = inventoryService;
        this.hospitalService = hospitalService;
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BloodInventoryDto>>> searchInventory(
            @RequestParam(value = "bloodGroup", required = false) String bloodGroupStr,
            @RequestParam(value = "componentType", required = false) String componentTypeStr,
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "districtId", required = false) Long districtId) {

        BloodGroup bloodGroup = null;
        if (bloodGroupStr != null && !bloodGroupStr.trim().isEmpty()) {
            bloodGroup = BloodGroup.fromLabel(bloodGroupStr);
            if (bloodGroup == null) {
                try {
                    bloodGroup = BloodGroup.valueOf(bloodGroupStr.trim().toUpperCase());
                } catch (IllegalArgumentException ignored) {}
            }
        }

        BloodComponentType componentType = null;
        if (componentTypeStr != null && !componentTypeStr.trim().isEmpty()) {
            try {
                componentType = BloodComponentType.valueOf(componentTypeStr.trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        List<BloodInventoryDto> results = inventoryService.searchAvailableInventory(
                bloodGroup, componentType, stateId, districtId);

        return ResponseEntity.ok(ApiResponse.ok("Live inventory search completed", results));
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<ApiResponse<List<BloodInventoryDto>>> getHospitalInventory(@PathVariable Long hospitalId) {
        List<BloodInventoryDto> items = inventoryService.getInventoryDtosByHospital(hospitalId);
        return ResponseEntity.ok(ApiResponse.ok(items));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<BloodInventoryDto>> updateStock(
            @Valid @RequestBody StockUpdateDto dto,
            Authentication authentication) {
        String email = authentication != null ? authentication.getName() : "ANONYMOUS";
        HospitalProfile hospital = hospitalService.getHospitalByUserEmail(email);

        BloodInventory saved = inventoryService.addOrUpdateStock(hospital.getId(), dto, email);
        return ResponseEntity.ok(ApiResponse.ok("Stock updated successfully", null));
    }

    @GetMapping("/low-stock-alerts")
    public ResponseEntity<ApiResponse<List<BloodInventoryDto>>> getLowStockAlerts(
            @RequestParam(value = "threshold", defaultValue = "5") int threshold) {
        List<BloodInventoryDto> alerts = inventoryService.getLowStockAlerts(threshold);
        return ResponseEntity.ok(ApiResponse.ok(alerts));
    }
}
