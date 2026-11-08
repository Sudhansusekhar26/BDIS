package com.bdis.controller;

import com.bdis.dto.HospitalRegistrationDto;
import com.bdis.dto.UserRegistrationDto;
import com.bdis.enums.BloodGroup;
import com.bdis.service.HospitalService;
import com.bdis.service.LocationService;
import com.bdis.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;
    private final HospitalService hospitalService;
    private final LocationService locationService;

    public AuthController(UserService userService, HospitalService hospitalService, LocationService locationService) {
        this.userService = userService;
        this.hospitalService = hospitalService;
        this.locationService = locationService;
    }

    @GetMapping("/login")
    public String loginPage(Authentication authentication) {
        // If already authenticated, redirect to their role home page
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
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(
            @RequestParam(value = "type", defaultValue = "user") String registrationType,
            Model model) {

        if (!model.containsAttribute("userDto")) {
            model.addAttribute("userDto", new UserRegistrationDto());
        }
        if (!model.containsAttribute("hospitalDto")) {
            model.addAttribute("hospitalDto", new HospitalRegistrationDto());
        }

        model.addAttribute("activeTab", registrationType);
        model.addAttribute("states", locationService.getAllStates());
        model.addAttribute("bloodGroups", BloodGroup.values());
        return "register";
    }

    @GetMapping("/hospital/register")
    public String hospitalRegisterRedirect() {
        return "redirect:/register?type=hospital";
    }


    @PostMapping("/register")
    public String processUserRegistration(
            @Valid @ModelAttribute("userDto") UserRegistrationDto dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("activeTab", "user");
            if (!model.containsAttribute("hospitalDto")) {
                model.addAttribute("hospitalDto", new HospitalRegistrationDto());
            }
            model.addAttribute("states", locationService.getAllStates());
            model.addAttribute("bloodGroups", BloodGroup.values());
            return "register";
        }

        try {
            userService.registerUser(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Account created successfully! Please sign in to your dashboard.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("activeTab", "user");
            if (!model.containsAttribute("hospitalDto")) {
                model.addAttribute("hospitalDto", new HospitalRegistrationDto());
            }
            model.addAttribute("states", locationService.getAllStates());
            model.addAttribute("bloodGroups", BloodGroup.values());
            return "register";
        }
    }

    @PostMapping("/hospital/register")
    public String processHospitalRegistration(
            @Valid @ModelAttribute("hospitalDto") HospitalRegistrationDto dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("activeTab", "hospital");
            if (!model.containsAttribute("userDto")) {
                model.addAttribute("userDto", new UserRegistrationDto());
            }
            model.addAttribute("states", locationService.getAllStates());
            model.addAttribute("bloodGroups", BloodGroup.values());
            return "register";
        }

        try {
            hospitalService.registerHospital(dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Hospital registration submitted! Your medical license is undergoing administrative verification. You may sign in once approved.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("activeTab", "hospital");
            if (!model.containsAttribute("userDto")) {
                model.addAttribute("userDto", new UserRegistrationDto());
            }
            model.addAttribute("states", locationService.getAllStates());
            model.addAttribute("bloodGroups", BloodGroup.values());
            return "register";
        }
    }
}
