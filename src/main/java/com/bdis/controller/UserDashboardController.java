package com.bdis.controller;

import com.bdis.dto.BloodRequestCreateDto;
import com.bdis.dto.BloodRequestResponseDto;
import com.bdis.entity.DonorPledge;
import com.bdis.entity.HospitalProfile;
import com.bdis.entity.User;
import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import com.bdis.enums.UrgencyLevel;
import com.bdis.service.BloodRequestService;
import com.bdis.service.HospitalService;
import com.bdis.service.UserService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserDashboardController {

    private final UserService userService;
    private final HospitalService hospitalService;
    private final BloodRequestService requestService;

    public UserDashboardController(UserService userService,
                                   HospitalService hospitalService,
                                   BloodRequestService requestService) {
        this.userService = userService;
        this.hospitalService = hospitalService;
        this.requestService = requestService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = userService.getUserByEmail(authentication.getName());
        List<BloodRequestResponseDto> myRequests = requestService.getRequestsByRequester(user.getId());
        List<DonorPledge> myPledges = userService.getDonorPledges(user.getId());

        model.addAttribute("currentUser", user);
        model.addAttribute("recentRequests", myRequests.stream().limit(5).toList());
        model.addAttribute("totalRequestsCount", myRequests.size());
        model.addAttribute("activeRequestsCount", myRequests.stream().filter(r -> "PENDING".equals(r.getStatus().name()) || "ACCEPTED".equals(r.getStatus().name()) || "DISPATCHED".equals(r.getStatus().name())).count());
        model.addAttribute("completedDeliveriesCount", myRequests.stream().filter(r -> "DELIVERED".equals(r.getStatus().name())).count());
        model.addAttribute("myPledges", myPledges);

        return "user/dashboard";
    }

    @GetMapping("/request-blood")
    public String requestBloodForm(
            @RequestParam(value = "hospitalId", required = false) Long preselectedHospitalId,
            @RequestParam(value = "bloodGroup", required = false) String preselectedBg,
            @RequestParam(value = "componentType", required = false) String preselectedComp,
            Authentication authentication,
            Model model) {

        User user = userService.getUserByEmail(authentication.getName());

        if (!model.containsAttribute("requestDto")) {
            BloodRequestCreateDto dto = BloodRequestCreateDto.builder()
                    .hospitalId(preselectedHospitalId)
                    .deliveryAddress(user.getAddress() != null ? user.getAddress() : "")
                    .build();

            if (preselectedBg != null) {
                BloodGroup bg = BloodGroup.fromLabel(preselectedBg);
                if (bg == null) {
                    try { bg = BloodGroup.valueOf(preselectedBg.toUpperCase()); } catch (Exception ignored) {}
                }
                dto.setBloodGroup(bg);
            }

            if (preselectedComp != null) {
                try {
                    dto.setComponentType(BloodComponentType.valueOf(preselectedComp.toUpperCase()));
                } catch (Exception ignored) {}
            }

            model.addAttribute("requestDto", dto);
        }

        List<HospitalProfile> hospitals = hospitalService.getVerifiedHospitals();
        model.addAttribute("hospitals", hospitals);
        model.addAttribute("bloodGroups", BloodGroup.values());
        model.addAttribute("componentTypes", BloodComponentType.values());
        model.addAttribute("urgencyLevels", UrgencyLevel.values());
        model.addAttribute("currentUser", user);

        return "request-blood";
    }

    @PostMapping("/request-blood")
    public String submitBloodRequest(
            @Valid @ModelAttribute("requestDto") BloodRequestCreateDto dto,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        User user = userService.getUserByEmail(authentication.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("hospitals", hospitalService.getVerifiedHospitals());
            model.addAttribute("bloodGroups", BloodGroup.values());
            model.addAttribute("componentTypes", BloodComponentType.values());
            model.addAttribute("urgencyLevels", UrgencyLevel.values());
            model.addAttribute("currentUser", user);
            return "request-blood";
        }

        try {
            requestService.createRequest(user.getId(), dto);
            redirectAttributes.addFlashAttribute("successMessage", "Urgent blood request registered successfully! The hospital has been notified.");
            return "redirect:/user/my-requests";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("hospitals", hospitalService.getVerifiedHospitals());
            model.addAttribute("bloodGroups", BloodGroup.values());
            model.addAttribute("componentTypes", BloodComponentType.values());
            model.addAttribute("urgencyLevels", UrgencyLevel.values());
            model.addAttribute("currentUser", user);
            return "request-blood";
        }
    }

    @GetMapping("/my-requests")
    public String myRequests(Authentication authentication, Model model) {
        User user = userService.getUserByEmail(authentication.getName());
        List<BloodRequestResponseDto> myRequests = requestService.getRequestsByRequester(user.getId());
        model.addAttribute("currentUser", user);
        model.addAttribute("myRequests", myRequests);
        return "user/my-requests";
    }

    @PostMapping("/requests/{id}/cancel")
    public String cancelRequest(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByEmail(authentication.getName());
        try {
            requestService.cancelRequest(id, user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Blood request #" + id + " was cancelled successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/user/my-requests";
    }

    @GetMapping("/pledge")
    public String donorPledgeForm(Authentication authentication, Model model) {
        User user = userService.getUserByEmail(authentication.getName());
        model.addAttribute("currentUser", user);
        model.addAttribute("hospitals", hospitalService.getVerifiedHospitals());
        model.addAttribute("myPledges", userService.getDonorPledges(user.getId()));
        return "user/donor-pledge";
    }

    @PostMapping("/pledge")
    public String submitDonorPledge(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId,
            @RequestParam("pledgedDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pledgedDate,
            @RequestParam(value = "notes", required = false) String notes,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = userService.getUserByEmail(authentication.getName());
        try {
            userService.createDonorPledge(user.getId(), hospitalId, pledgedDate, notes);
            redirectAttributes.addFlashAttribute("successMessage", "Thank you for pledging to save lives! Your donation appointment is registered.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/user/pledge";
    }
}
