package com.bdis.controller.api;

import com.bdis.dto.ApiResponse;
import com.bdis.dto.DashboardStatsDto;
import com.bdis.entity.HospitalProfile;
import com.bdis.service.AnalyticsService;
import com.bdis.service.HospitalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsApiController {

    private final AnalyticsService analyticsService;
    private final HospitalService hospitalService;

    public AnalyticsApiController(AnalyticsService analyticsService, HospitalService hospitalService) {
        this.analyticsService = analyticsService;
        this.hospitalService = hospitalService;
    }

    @GetMapping("/admin-stats")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getAdminStats() {
        DashboardStatsDto stats = analyticsService.getAdminDashboardStats();
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }

    @GetMapping("/hospital-stats/{hospitalId}")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getHospitalStats(@PathVariable Long hospitalId) {
        DashboardStatsDto stats = analyticsService.getHospitalDashboardStats(hospitalId);
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }

    @GetMapping("/my-hospital-stats")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getMyHospitalStats(Authentication authentication) {
        String email = authentication.getName();
        HospitalProfile hospital = hospitalService.getHospitalByUserEmail(email);
        DashboardStatsDto stats = analyticsService.getHospitalDashboardStats(hospital.getId());
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }
}
