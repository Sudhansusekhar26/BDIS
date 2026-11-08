package com.bdis.service.impl;

import com.bdis.dto.DashboardStatsDto;
import com.bdis.entity.BloodInventory;
import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import com.bdis.enums.RequestStatus;
import com.bdis.enums.Role;
import com.bdis.repository.BloodInventoryRepository;
import com.bdis.repository.BloodRequestRepository;
import com.bdis.repository.HospitalProfileRepository;
import com.bdis.repository.UserRepository;
import com.bdis.service.AnalyticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    private final BloodInventoryRepository inventoryRepository;
    private final HospitalProfileRepository hospitalRepository;
    private final UserRepository userRepository;
    private final BloodRequestRepository requestRepository;

    public AnalyticsServiceImpl(BloodInventoryRepository inventoryRepository,
                                HospitalProfileRepository hospitalRepository,
                                UserRepository userRepository,
                                BloodRequestRepository requestRepository) {
        this.inventoryRepository = inventoryRepository;
        this.hospitalRepository = hospitalRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public DashboardStatsDto getAdminDashboardStats() {
        long totalUnits = inventoryRepository.getTotalAvailableUnits();
        long totalHospitals = hospitalRepository.countByIsVerified(true);
        long pendingHospitals = hospitalRepository.countByIsVerified(false);
        long totalUsers = userRepository.countByRole(Role.ROLE_USER);
        long totalRequests = requestRepository.count();
        long pendingRequests = requestRepository.countByStatus(RequestStatus.PENDING);
        long activeDispatches = requestRepository.countByStatus(RequestStatus.DISPATCHED);
        long completedDeliveries = requestRepository.countByStatus(RequestStatus.DELIVERED);
        long criticalShortages = inventoryRepository.findLowStockItems(5).size();

        Map<String, Integer> unitsByBg = new LinkedHashMap<>();
        for (BloodGroup bg : BloodGroup.values()) {
            unitsByBg.put(bg.getLabel(), 0);
        }
        List<Object[]> bgResults = inventoryRepository.getUnitsCountByBloodGroup();
        for (Object[] row : bgResults) {
            if (row[0] != null && row[1] != null) {
                BloodGroup bg = (BloodGroup) row[0];
                Number count = (Number) row[1];
                unitsByBg.put(bg.getLabel(), count.intValue());
            }
        }

        Map<String, Integer> unitsByComp = new LinkedHashMap<>();
        for (BloodComponentType comp : BloodComponentType.values()) {
            unitsByComp.put(comp.getDisplayName(), 0);
        }
        List<Object[]> compResults = inventoryRepository.getUnitsCountByComponentType();
        for (Object[] row : compResults) {
            if (row[0] != null && row[1] != null) {
                BloodComponentType comp = (BloodComponentType) row[0];
                Number count = (Number) row[1];
                unitsByComp.put(comp.getDisplayName(), count.intValue());
            }
        }

        return DashboardStatsDto.builder()
                .totalAvailableUnits(totalUnits)
                .totalHospitals(totalHospitals)
                .pendingHospitalVerifications(pendingHospitals)
                .totalUsers(totalUsers)
                .totalRequests(totalRequests)
                .pendingRequests(pendingRequests)
                .activeDispatches(activeDispatches)
                .completedDeliveries(completedDeliveries)
                .criticalShortages(criticalShortages)
                .unitsByBloodGroup(unitsByBg)
                .unitsByComponent(unitsByComp)
                .build();
    }

    @Override
    public DashboardStatsDto getHospitalDashboardStats(Long hospitalId) {
        List<BloodInventory> hospitalItems = inventoryRepository.findByHospital_Id(hospitalId);

        long totalUnits = hospitalItems.stream().mapToInt(BloodInventory::getUnitsAvailable).sum();
        long pendingRequests = requestRepository.countByHospital_IdAndStatus(hospitalId, RequestStatus.PENDING);
        long activeDispatches = requestRepository.countByHospital_IdAndStatus(hospitalId, RequestStatus.DISPATCHED);
        long completedDeliveries = requestRepository.countByHospital_IdAndStatus(hospitalId, RequestStatus.DELIVERED);
        long lowStockCount = hospitalItems.stream().filter(BloodInventory::isLowStock).count();

        Map<String, Integer> unitsByBg = new LinkedHashMap<>();
        for (BloodGroup bg : BloodGroup.values()) {
            unitsByBg.put(bg.getLabel(), 0);
        }
        for (BloodInventory item : hospitalItems) {
            unitsByBg.put(item.getBloodGroup().getLabel(),
                    unitsByBg.getOrDefault(item.getBloodGroup().getLabel(), 0) + item.getUnitsAvailable());
        }

        Map<String, Integer> unitsByComp = new LinkedHashMap<>();
        for (BloodComponentType comp : BloodComponentType.values()) {
            unitsByComp.put(comp.getDisplayName(), 0);
        }
        for (BloodInventory item : hospitalItems) {
            unitsByComp.put(item.getComponentType().getDisplayName(),
                    unitsByComp.getOrDefault(item.getComponentType().getDisplayName(), 0) + item.getUnitsAvailable());
        }

        return DashboardStatsDto.builder()
                .totalAvailableUnits(totalUnits)
                .pendingRequests(pendingRequests)
                .activeDispatches(activeDispatches)
                .completedDeliveries(completedDeliveries)
                .criticalShortages(lowStockCount)
                .unitsByBloodGroup(unitsByBg)
                .unitsByComponent(unitsByComp)
                .build();
    }
}
