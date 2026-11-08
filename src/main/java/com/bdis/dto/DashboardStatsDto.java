package com.bdis.dto;

import java.util.Map;

public class DashboardStatsDto {

    private long totalAvailableUnits;
    private long totalHospitals;
    private long pendingHospitalVerifications;
    private long totalUsers;
    private long totalRequests;
    private long pendingRequests;
    private long activeDispatches;
    private long completedDeliveries;
    private long criticalShortages;

    private Map<String, Integer> unitsByBloodGroup;
    private Map<String, Integer> unitsByComponent;

    public DashboardStatsDto() {}

    public DashboardStatsDto(long totalAvailableUnits, long totalHospitals, long pendingHospitalVerifications,
                             long totalUsers, long totalRequests, long pendingRequests, long activeDispatches,
                             long completedDeliveries, long criticalShortages,
                             Map<String, Integer> unitsByBloodGroup, Map<String, Integer> unitsByComponent) {
        this.totalAvailableUnits = totalAvailableUnits;
        this.totalHospitals = totalHospitals;
        this.pendingHospitalVerifications = pendingHospitalVerifications;
        this.totalUsers = totalUsers;
        this.totalRequests = totalRequests;
        this.pendingRequests = pendingRequests;
        this.activeDispatches = activeDispatches;
        this.completedDeliveries = completedDeliveries;
        this.criticalShortages = criticalShortages;
        this.unitsByBloodGroup = unitsByBloodGroup;
        this.unitsByComponent = unitsByComponent;
    }

    public long getTotalAvailableUnits() { return totalAvailableUnits; }
    public void setTotalAvailableUnits(long totalAvailableUnits) { this.totalAvailableUnits = totalAvailableUnits; }

    public long getTotalHospitals() { return totalHospitals; }
    public void setTotalHospitals(long totalHospitals) { this.totalHospitals = totalHospitals; }

    public long getPendingHospitalVerifications() { return pendingHospitalVerifications; }
    public void setPendingHospitalVerifications(long pendingHospitalVerifications) { this.pendingHospitalVerifications = pendingHospitalVerifications; }

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalRequests() { return totalRequests; }
    public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }

    public long getPendingRequests() { return pendingRequests; }
    public void setPendingRequests(long pendingRequests) { this.pendingRequests = pendingRequests; }

    public long getActiveDispatches() { return activeDispatches; }
    public void setActiveDispatches(long activeDispatches) { this.activeDispatches = activeDispatches; }

    public long getCompletedDeliveries() { return completedDeliveries; }
    public void setCompletedDeliveries(long completedDeliveries) { this.completedDeliveries = completedDeliveries; }

    public long getCriticalShortages() { return criticalShortages; }
    public void setCriticalShortages(long criticalShortages) { this.criticalShortages = criticalShortages; }

    public Map<String, Integer> getUnitsByBloodGroup() { return unitsByBloodGroup; }
    public void setUnitsByBloodGroup(Map<String, Integer> unitsByBloodGroup) { this.unitsByBloodGroup = unitsByBloodGroup; }

    public Map<String, Integer> getUnitsByComponent() { return unitsByComponent; }
    public void setUnitsByComponent(Map<String, Integer> unitsByComponent) { this.unitsByComponent = unitsByComponent; }

    public static DashboardStatsDtoBuilder builder() { return new DashboardStatsDtoBuilder(); }

    public static class DashboardStatsDtoBuilder {
        private long totalAvailableUnits;
        private long totalHospitals;
        private long pendingHospitalVerifications;
        private long totalUsers;
        private long totalRequests;
        private long pendingRequests;
        private long activeDispatches;
        private long completedDeliveries;
        private long criticalShortages;
        private Map<String, Integer> unitsByBloodGroup;
        private Map<String, Integer> unitsByComponent;

        public DashboardStatsDtoBuilder totalAvailableUnits(long totalAvailableUnits) { this.totalAvailableUnits = totalAvailableUnits; return this; }
        public DashboardStatsDtoBuilder totalHospitals(long totalHospitals) { this.totalHospitals = totalHospitals; return this; }
        public DashboardStatsDtoBuilder pendingHospitalVerifications(long pendingHospitalVerifications) { this.pendingHospitalVerifications = pendingHospitalVerifications; return this; }
        public DashboardStatsDtoBuilder totalUsers(long totalUsers) { this.totalUsers = totalUsers; return this; }
        public DashboardStatsDtoBuilder totalRequests(long totalRequests) { this.totalRequests = totalRequests; return this; }
        public DashboardStatsDtoBuilder pendingRequests(long pendingRequests) { this.pendingRequests = pendingRequests; return this; }
        public DashboardStatsDtoBuilder activeDispatches(long activeDispatches) { this.activeDispatches = activeDispatches; return this; }
        public DashboardStatsDtoBuilder completedDeliveries(long completedDeliveries) { this.completedDeliveries = completedDeliveries; return this; }
        public DashboardStatsDtoBuilder criticalShortages(long criticalShortages) { this.criticalShortages = criticalShortages; return this; }
        public DashboardStatsDtoBuilder unitsByBloodGroup(Map<String, Integer> unitsByBloodGroup) { this.unitsByBloodGroup = unitsByBloodGroup; return this; }
        public DashboardStatsDtoBuilder unitsByComponent(Map<String, Integer> unitsByComponent) { this.unitsByComponent = unitsByComponent; return this; }

        public DashboardStatsDto build() {
            return new DashboardStatsDto(totalAvailableUnits, totalHospitals, pendingHospitalVerifications, totalUsers, totalRequests, pendingRequests, activeDispatches, completedDeliveries, criticalShortages, unitsByBloodGroup, unitsByComponent);
        }
    }
}
