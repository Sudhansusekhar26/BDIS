package com.bdis.service.impl;

import com.bdis.dto.BloodInventoryDto;
import com.bdis.dto.StockUpdateDto;
import com.bdis.entity.BloodInventory;
import com.bdis.entity.HospitalProfile;
import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import com.bdis.exception.InsufficientBloodStockException;
import com.bdis.exception.InvalidOperationException;
import com.bdis.exception.ResourceNotFoundException;
import com.bdis.repository.BloodInventoryRepository;
import com.bdis.service.AuditLogService;
import com.bdis.service.BloodInventoryService;
import com.bdis.service.HospitalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BloodInventoryServiceImpl implements BloodInventoryService {

    private static final Logger log = LoggerFactory.getLogger(BloodInventoryServiceImpl.class);

    private final BloodInventoryRepository inventoryRepository;
    private final HospitalService hospitalService;
    private final AuditLogService auditLogService;

    public BloodInventoryServiceImpl(BloodInventoryRepository inventoryRepository,
                                     HospitalService hospitalService,
                                     AuditLogService auditLogService) {
        this.inventoryRepository = inventoryRepository;
        this.hospitalService = hospitalService;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public BloodInventory addOrUpdateStock(Long hospitalId, StockUpdateDto dto, String performedBy) {
        HospitalProfile hospital = hospitalService.getHospitalById(hospitalId);

        String batchNumber = (dto.getBatchNumber() != null && !dto.getBatchNumber().trim().isEmpty())
                ? dto.getBatchNumber().trim().toUpperCase()
                : "BATCH-" + System.currentTimeMillis();

        LocalDate expiryDate = dto.getExpiryDate();
        if (expiryDate == null) {
            expiryDate = LocalDate.now().plusDays(dto.getComponentType().getShelfLifeDays());
        }

        Optional<BloodInventory> existingOpt = inventoryRepository
                .findByHospital_IdAndBloodGroupAndComponentTypeAndBatchNumber(
                        hospitalId, dto.getBloodGroup(), dto.getComponentType(), batchNumber);

        BloodInventory inventory;
        if (existingOpt.isPresent()) {
            inventory = existingOpt.get();
            if ("INCREMENT".equalsIgnoreCase(dto.getOperation()) && dto.getDeltaUnits() != null) {
                inventory.setUnitsAvailable(inventory.getUnitsAvailable() + dto.getDeltaUnits());
            } else if ("DECREMENT".equalsIgnoreCase(dto.getOperation()) && dto.getDeltaUnits() != null) {
                int newUnits = inventory.getUnitsAvailable() - dto.getDeltaUnits();
                if (newUnits < 0) {
                    throw new InvalidOperationException("Cannot decrement stock below zero.");
                }
                inventory.setUnitsAvailable(newUnits);
            } else {
                inventory.setUnitsAvailable(dto.getUnitsAvailable());
            }
            inventory.setExpiryDate(expiryDate);
        } else {
            inventory = BloodInventory.builder()
                    .hospital(hospital)
                    .bloodGroup(dto.getBloodGroup())
                    .componentType(dto.getComponentType())
                    .unitsAvailable(dto.getUnitsAvailable() != null ? dto.getUnitsAvailable() : 0)
                    .expiryDate(expiryDate)
                    .batchNumber(batchNumber)
                    .build();
        }

        BloodInventory saved = inventoryRepository.save(inventory);
        auditLogService.log("STOCK_UPDATE", performedBy, "BloodInventory", String.valueOf(saved.getId()),
                "Stock updated: " + saved.getBloodGroup().getLabel() + " (" + saved.getComponentType().getDisplayName() + ") Units: " + saved.getUnitsAvailable());
        return saved;
    }

    @Override
    @Transactional
    public BloodInventory adjustStockUnits(Long inventoryId, int deltaUnits, String reason, String performedBy) {
        BloodInventory inventory = getInventoryById(inventoryId);
        int newTotal = inventory.getUnitsAvailable() + deltaUnits;
        if (newTotal < 0) {
            throw new InvalidOperationException("Stock reduction cannot result in negative units.");
        }
        inventory.setUnitsAvailable(newTotal);
        BloodInventory saved = inventoryRepository.save(inventory);
        auditLogService.log("STOCK_ADJUSTMENT", performedBy, "BloodInventory", String.valueOf(inventoryId),
                "Units adjusted by " + deltaUnits + " (New Total: " + newTotal + "). Reason: " + reason);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodInventory> getInventoryByHospital(Long hospitalId) {
        return inventoryRepository.findByHospital_IdOrderByBloodGroupAsc(hospitalId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodInventoryDto> getInventoryDtosByHospital(Long hospitalId) {
        return getInventoryByHospital(hospitalId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodInventoryDto> searchAvailableInventory(
            BloodGroup bloodGroup, BloodComponentType componentType, Long stateId, Long districtId) {
        return inventoryRepository.searchAvailableInventory(bloodGroup, componentType, stateId, districtId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BloodInventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blood inventory item not found with ID: " + id));
    }

    @Override
    @Transactional
    public void deductStockForRequest(Long hospitalId, BloodGroup bloodGroup, BloodComponentType componentType, int unitsNeeded) {
        List<BloodInventory> batches = inventoryRepository.findByHospital_IdAndBloodGroupAndComponentType(hospitalId, bloodGroup, componentType);

        int totalAvailable = batches.stream()
                .filter(b -> !b.isExpired())
                .mapToInt(BloodInventory::getUnitsAvailable)
                .sum();

        if (totalAvailable < unitsNeeded) {
            throw new InsufficientBloodStockException(
                    "Insufficient stock for " + bloodGroup.getLabel() + " (" + componentType.getDisplayName() + "). Available: " + totalAvailable + " units, Requested: " + unitsNeeded + " units.");
        }

        // Deduct from batches sorted by earliest expiry date (FIFO)
        batches.sort(Comparator.comparing(BloodInventory::getExpiryDate, Comparator.nullsLast(Comparator.naturalOrder())));

        int remaining = unitsNeeded;
        for (BloodInventory batch : batches) {
            if (batch.isExpired() || batch.getUnitsAvailable() <= 0) {
                continue;
            }
            int availableInBatch = batch.getUnitsAvailable();
            if (availableInBatch >= remaining) {
                batch.setUnitsAvailable(availableInBatch - remaining);
                inventoryRepository.save(batch);
                remaining = 0;
                break;
            } else {
                batch.setUnitsAvailable(0);
                remaining -= availableInBatch;
                inventoryRepository.save(batch);
            }
        }

        if (remaining > 0) {
            throw new InsufficientBloodStockException("Failed to allocate required units across available batches.");
        }
    }

    @Override
    @Transactional
    public void restoreStockForCancelledRequest(Long hospitalId, BloodGroup bloodGroup, BloodComponentType componentType, int units) {
        List<BloodInventory> batches = inventoryRepository.findByHospital_IdAndBloodGroupAndComponentType(hospitalId, bloodGroup, componentType);
        if (!batches.isEmpty()) {
            BloodInventory firstBatch = batches.get(0);
            firstBatch.setUnitsAvailable(firstBatch.getUnitsAvailable() + units);
            inventoryRepository.save(firstBatch);
        } else {
            StockUpdateDto dto = StockUpdateDto.builder()
                    .bloodGroup(bloodGroup)
                    .componentType(componentType)
                    .unitsAvailable(units)
                    .batchNumber("RESTORED-" + System.currentTimeMillis())
                    .build();
            addOrUpdateStock(hospitalId, dto, "SYSTEM");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int getAvailableStockCount(Long hospitalId, BloodGroup bloodGroup, BloodComponentType componentType) {
        return inventoryRepository.sumAvailableUnitsForHospital(hospitalId, bloodGroup, componentType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloodInventoryDto> getLowStockAlerts(int threshold) {
        return inventoryRepository.findLowStockItems(threshold).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteInventoryItem(Long inventoryId, String performedBy) {
        BloodInventory item = getInventoryById(inventoryId);
        inventoryRepository.delete(item);
        auditLogService.log("STOCK_DELETE", performedBy, "BloodInventory", String.valueOf(inventoryId), "Deleted inventory batch: " + item.getBatchNumber());
    }

    private BloodInventoryDto mapToDto(BloodInventory item) {
        return BloodInventoryDto.builder()
                .id(item.getId())
                .hospitalId(item.getHospital().getId())
                .hospitalName(item.getHospital().getHospitalName())
                .hospitalHelpline(item.getHospital().getEmergencyHelpline())
                .hospitalAddress(item.getHospital().getAddress())
                .districtName(item.getHospital().getDistrict() != null ? item.getHospital().getDistrict().getName() : "")
                .stateName(item.getHospital().getState() != null ? item.getHospital().getState().getName() : "")
                .bloodGroup(item.getBloodGroup())
                .bloodGroupLabel(item.getBloodGroup().getLabel())
                .componentType(item.getComponentType())
                .componentName(item.getComponentType().getDisplayName())
                .unitsAvailable(item.getUnitsAvailable())
                .expiryDate(item.getExpiryDate())
                .batchNumber(item.getBatchNumber())
                .lowStock(item.isLowStock())
                .outOfStock(item.isOutOfStock())
                .expired(item.isExpired())
                .lastUpdated(item.getLastUpdated())
                .build();
    }
}
