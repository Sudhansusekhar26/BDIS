package com.bdis.service;

import com.bdis.dto.DashboardStatsDto;

public interface AnalyticsService {

    DashboardStatsDto getAdminDashboardStats();

    DashboardStatsDto getHospitalDashboardStats(Long hospitalId);
}
