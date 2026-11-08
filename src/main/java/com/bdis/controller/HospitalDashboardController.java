package com.bdis.controller;

import com.bdis.dto.BloodInventoryDto;
import com.bdis.dto.BloodRequestResponseDto;
import com.bdis.dto.DashboardStatsDto;
import com.bdis.dto.DeliveryUpdateDto;
import com.bdis.dto.StockUpdateDto;
import com.bdis.entity.HospitalProfile;
import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import com.bdis.enums.DeliveryStatus;
import com.bdis.enums.RequestStatus;
import com.bdis.service.AnalyticsService;
import com.bdis.service.BloodInventoryService;
import com.bdis.service.BloodRequestService;
import com.bdis.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/hospital")
public class HospitalDashboardController {

    private final HospitalService hospitalService;
    private final BloodInventoryService inventoryService;
    private final BloodRequestService requestService;
    private final AnalyticsService analyticsService;

    public HospitalDashboardController(HospitalService hospitalService,
                                       BloodInventoryService inventoryService,
                                       BloodRequestService requestService,
                                       AnalyticsService analyticsService) {
        this.hospitalService = hospitalService;
        this.inventoryService = inventoryService;
        this.requestService = requestService;
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        HospitalProfile hospital = hospitalService.getHospitalByUserEmail(authentication.getName());

        if (!hospital.isVerified()) {
            model.addAttribute("hospital", hospital);
            return "hospital/pending-verification";
        }

        DashboardStatsDto stats = analyticsService.getHospitalDashboardStats(hospital.getId());
        List<BloodRequestResponseDto> pendingRequests = requestService.getRequestsByHospitalAndStatus(hospital.getId(), RequestStatus.PENDING);
        List<BloodInventoryDto> inventoryItems = inventoryService.getInventoryDtosByHospital(hospital.getId());

        model.addAttribute("hospital", hospital);
        model.addAttribute("stats", stats);
        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("inventoryItems", inventoryItems);

        return "hospital/dashboard";
    }

    @GetMapping("/inventory")
    public String inventoryManager(Authentication authentication, Model model) {
        HospitalProfile hospital = hospitalService.getHospitalByUserEmail(authentication.getName());
        List<BloodInventoryDto> inventoryItems = inventoryService.getInventoryDtosByHospital(hospital.getId());

        if (!model.containsAttribute("stockDto")) {
            model.addAttribute("stockDto", new StockUpdateDto());
        }

        model.addAttribute("hospital", hospital);
        model.addAttribute("inventoryItems", inventoryItems);
        model.addAttribute("bloodGroups", BloodGroup.values());
        model.addAttribute("componentTypes", BloodComponentType.values());

        return "hospital/inventory";
    }

    @PostMapping("/inventory/add")
    public String addOrUpdateStock(
            @Valid @ModelAttribute("stockDto") StockUpdateDto dto,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        HospitalProfile hospital = hospitalService.getHospitalByUserEmail(authentication.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("hospital", hospital);
            model.addAttribute("inventoryItems", inventoryService.getInventoryDtosByHospital(hospital.getId()));
            model.addAttribute("bloodGroups", BloodGroup.values());
            model.addAttribute("componentTypes", BloodComponentType.values());
            return "hospital/inventory";
        }

        try {
            inventoryService.addOrUpdateStock(hospital.getId(), dto, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Stock updated for " + dto.getBloodGroup().getLabel() + " (" + dto.getComponentType().getDisplayName() + ")");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/hospital/inventory";
    }

    @PostMapping("/inventory/{id}/adjust")
    public String adjustStock(
            @PathVariable Long id,
            @RequestParam("delta") int deltaUnits,
            @RequestParam(value = "reason", defaultValue = "Routine manual adjustment") String reason,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            inventoryService.adjustStockUnits(id, deltaUnits, reason, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Stock quantity adjusted successfully (" + (deltaUnits > 0 ? "+" : "") + deltaUnits + " units).");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/hospital/inventory";
    }

    @PostMapping("/inventory/{id}/delete")
    public String deleteStockBatch(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            inventoryService.deleteInventoryItem(id, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Inventory batch discarded / written off.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/hospital/inventory";
    }

    @GetMapping("/requests")
    public String requestsPipeline(
            @RequestParam(value = "status", required = false) String statusStr,
            Authentication authentication,
            Model model) {

        HospitalProfile hospital = hospitalService.getHospitalByUserEmail(authentication.getName());

        List<BloodRequestResponseDto> requests;
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                RequestStatus status = RequestStatus.valueOf(statusStr.toUpperCase());
                requests = requestService.getRequestsByHospitalAndStatus(hospital.getId(), status);
            } catch (IllegalArgumentException e) {
                requests = requestService.getRequestsByHospital(hospital.getId());
            }
        } else {
            requests = requestService.getRequestsByHospital(hospital.getId());
        }

        model.addAttribute("hospital", hospital);
        model.addAttribute("requests", requests);
        model.addAttribute("currentStatusFilter", statusStr);
        model.addAttribute("requestStatuses", RequestStatus.values());

        return "hospital/requests";
    }

    @PostMapping("/requests/{id}/accept")
    public String acceptRequest(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            requestService.acceptRequest(id, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Request #" + id + " accepted! Required units have been securely locked and deducted from inventory.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/hospital/requests";
    }

    @PostMapping("/requests/{id}/reject")
    public String rejectRequest(
            @PathVariable Long id,
            @RequestParam(value = "reason", required = false) String reason,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            requestService.rejectRequest(id, reason, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Request #" + id + " rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/hospital/requests";
    }

    @PostMapping("/requests/{id}/dispatch")
    public String dispatchRequest(
            @PathVariable Long id,
            @Valid @ModelAttribute DeliveryUpdateDto dto,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            dto.setCurrentStatus(DeliveryStatus.IN_TRANSIT);
            requestService.updateDeliveryInfo(id, dto, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Transit partner assigned and blood bag dispatched for Request #" + id + "!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/hospital/requests";
    }

    @PostMapping("/requests/{id}/deliver")
    public String markDelivered(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            requestService.updateStatus(id, RequestStatus.DELIVERED, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Request #" + id + " marked as DELIVERED!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/hospital/requests";
    }
}
