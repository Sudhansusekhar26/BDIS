package com.bdis.controller;

import com.bdis.dto.BloodInventoryDto;
import com.bdis.dto.BloodRequestResponseDto;
import com.bdis.dto.DashboardStatsDto;
import com.bdis.entity.AuditLog;
import com.bdis.entity.HospitalProfile;
import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import com.bdis.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final AnalyticsService analyticsService;
    private final HospitalService hospitalService;
    private final BloodInventoryService inventoryService;
    private final BloodRequestService requestService;
    private final AuditLogService auditLogService;
    private final LocationService locationService;

    public AdminDashboardController(AnalyticsService analyticsService,
                                    HospitalService hospitalService,
                                    BloodInventoryService inventoryService,
                                    BloodRequestService requestService,
                                    AuditLogService auditLogService,
                                    LocationService locationService) {
        this.analyticsService = analyticsService;
        this.hospitalService = hospitalService;
        this.inventoryService = inventoryService;
        this.requestService = requestService;
        this.auditLogService = auditLogService;
        this.locationService = locationService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        DashboardStatsDto stats = analyticsService.getAdminDashboardStats();
        List<HospitalProfile> pendingHospitals = hospitalService.getPendingHospitals();
        List<BloodRequestResponseDto> recentRequests = requestService.getAllRequests().stream().limit(6).toList();
        List<BloodInventoryDto> lowStockAlerts = inventoryService.getLowStockAlerts(5);
        List<AuditLog> recentLogs = auditLogService.getRecentLogs().stream().limit(10).toList();

        model.addAttribute("stats", stats);
        model.addAttribute("pendingHospitals", pendingHospitals);
        model.addAttribute("recentRequests", recentRequests);
        model.addAttribute("lowStockAlerts", lowStockAlerts);
        model.addAttribute("recentLogs", recentLogs);

        return "admin/dashboard";
    }

    @GetMapping("/hospitals")
    public String hospitalVerificationList(Model model) {
        List<HospitalProfile> allHospitals = hospitalService.getAllHospitals();
        model.addAttribute("hospitals", allHospitals);
        return "admin/hospitals";
    }

    @PostMapping("/hospitals/{id}/verify")
    public String verifyHospital(
            @PathVariable Long id,
            @RequestParam("approved") boolean approved,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            hospitalService.verifyHospital(id, approved);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Hospital verification status updated (" + (approved ? "APPROVED" : "REJECTED") + ").");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/hospitals";
    }

    @GetMapping("/inventory")
    public String inventoryOverrideView(
            @RequestParam(value = "bloodGroup", required = false) String bloodGroupStr,
            @RequestParam(value = "componentType", required = false) String componentTypeStr,
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "districtId", required = false) Long districtId,
            Model model) {

        BloodGroup bloodGroup = BloodGroup.fromLabel(bloodGroupStr);
        if (bloodGroup == null && bloodGroupStr != null && !bloodGroupStr.isEmpty()) {
            try { bloodGroup = BloodGroup.valueOf(bloodGroupStr.toUpperCase()); } catch (Exception ignored) {}
        }

        BloodComponentType componentType = null;
        if (componentTypeStr != null && !componentTypeStr.isEmpty()) {
            try { componentType = BloodComponentType.valueOf(componentTypeStr.toUpperCase()); } catch (Exception ignored) {}
        }

        List<BloodInventoryDto> inventory = inventoryService.searchAvailableInventory(bloodGroup, componentType, stateId, districtId);

        model.addAttribute("inventory", inventory);
        model.addAttribute("states", locationService.getAllStates());
        model.addAttribute("bloodGroups", BloodGroup.values());
        model.addAttribute("componentTypes", BloodComponentType.values());
        model.addAttribute("selectedStateId", stateId);
        model.addAttribute("selectedDistrictId", districtId);
        model.addAttribute("selectedBloodGroup", bloodGroupStr);
        model.addAttribute("selectedComponentType", componentTypeStr);

        return "admin/inventory-override";
    }

    @PostMapping("/inventory/{id}/override")
    public String emergencyStockOverride(
            @PathVariable Long id,
            @RequestParam("units") int newUnits,
            @RequestParam("reason") String reason,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            int current = inventoryService.getInventoryById(id).getUnitsAvailable();
            inventoryService.adjustStockUnits(id, newUnits - current, "ADMIN_OVERRIDE: " + reason, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Emergency stock adjustment committed.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/inventory";
    }

    @GetMapping("/requests")
    public String allRequests(Model model) {
        List<BloodRequestResponseDto> requests = requestService.getAllRequests();
        model.addAttribute("requests", requests);
        return "admin/requests";
    }

    @GetMapping("/audit-logs")
    public String auditLogs(Model model) {
        List<AuditLog> logs = auditLogService.getAllLogs();
        model.addAttribute("logs", logs);
        return "admin/audit-logs";
    }
}
