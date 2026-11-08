package com.bdis.service;

import com.bdis.dto.BloodInventoryDto;
import com.bdis.dto.StockUpdateDto;
import com.bdis.entity.BloodInventory;
import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;

import java.util.List;

public interface BloodInventoryService {

    BloodInventory addOrUpdateStock(Long hospitalId, StockUpdateDto dto, String performedBy);

    BloodInventory adjustStockUnits(Long inventoryId, int deltaUnits, String reason, String performedBy);

    List<BloodInventory> getInventoryByHospital(Long hospitalId);

    List<BloodInventoryDto> getInventoryDtosByHospital(Long hospitalId);

    List<BloodInventoryDto> searchAvailableInventory(BloodGroup bloodGroup, BloodComponentType componentType, Long stateId, Long districtId);

    BloodInventory getInventoryById(Long id);

    void deductStockForRequest(Long hospitalId, BloodGroup bloodGroup, BloodComponentType componentType, int units);

    void restoreStockForCancelledRequest(Long hospitalId, BloodGroup bloodGroup, BloodComponentType componentType, int units);

    int getAvailableStockCount(Long hospitalId, BloodGroup bloodGroup, BloodComponentType componentType);

    List<BloodInventoryDto> getLowStockAlerts(int threshold);

    void deleteInventoryItem(Long inventoryId, String performedBy);
}
