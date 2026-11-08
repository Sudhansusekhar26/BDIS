package com.bdis.service;

import com.bdis.dto.StockUpdateDto;
import com.bdis.entity.BloodInventory;
import com.bdis.entity.HospitalProfile;
import com.bdis.enums.BloodComponentType;
import com.bdis.enums.BloodGroup;
import com.bdis.exception.InsufficientBloodStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BloodInventoryServiceTest {

    @Autowired
    private BloodInventoryService inventoryService;

    @Autowired
    private HospitalService hospitalService;

    @Test
    @DisplayName("Should successfully add stock and retrieve by hospital")
    void testAddAndRetrieveStock() {
        List<HospitalProfile> hospitals = hospitalService.getVerifiedHospitals();
        assertFalse(hospitals.isEmpty(), "Verified hospitals should exist from seed data");
        HospitalProfile hospital = hospitals.get(0);

        StockUpdateDto dto = StockUpdateDto.builder()
                .bloodGroup(BloodGroup.AB_NEG)
                .componentType(BloodComponentType.PLATELETS)
                .unitsAvailable(25)
                .batchNumber("TEST-BATCH-ABNEG-01")
                .expiryDate(LocalDate.now().plusDays(5))
                .build();

        BloodInventory saved = inventoryService.addOrUpdateStock(hospital.getId(), dto, "TEST_RUNNER");
        assertNotNull(saved.getId());
        assertEquals(25, saved.getUnitsAvailable());
        assertEquals(BloodGroup.AB_NEG, saved.getBloodGroup());
        assertEquals(BloodComponentType.PLATELETS, saved.getComponentType());

        int available = inventoryService.getAvailableStockCount(hospital.getId(), BloodGroup.AB_NEG, BloodComponentType.PLATELETS);
        assertTrue(available >= 25);
    }

    @Test
    @DisplayName("Should deduct stock units atomically")
    void testDeductStockSuccess() {
        HospitalProfile hospital = hospitalService.getVerifiedHospitals().get(0);

        // Ensure we have at least 10 units of A_POS WHOLE_BLOOD
        StockUpdateDto dto = StockUpdateDto.builder()
                .bloodGroup(BloodGroup.A_POS)
                .componentType(BloodComponentType.WHOLE_BLOOD)
                .unitsAvailable(10)
                .batchNumber("TEST-DEDUCT-BATCH-01")
                .expiryDate(LocalDate.now().plusDays(30))
                .build();
        inventoryService.addOrUpdateStock(hospital.getId(), dto, "TEST_RUNNER");

        int initialStock = inventoryService.getAvailableStockCount(hospital.getId(), BloodGroup.A_POS, BloodComponentType.WHOLE_BLOOD);
        assertTrue(initialStock >= 5);

        inventoryService.deductStockForRequest(hospital.getId(), BloodGroup.A_POS, BloodComponentType.WHOLE_BLOOD, 3);

        int postStock = inventoryService.getAvailableStockCount(hospital.getId(), BloodGroup.A_POS, BloodComponentType.WHOLE_BLOOD);
        assertEquals(initialStock - 3, postStock);
    }

    @Test
    @DisplayName("Should throw InsufficientBloodStockException when units exceed available stock")
    void testDeductStockInsufficient() {
        HospitalProfile hospital = hospitalService.getVerifiedHospitals().get(0);
        int currentStock = inventoryService.getAvailableStockCount(hospital.getId(), BloodGroup.O_NEG, BloodComponentType.PLASMA);

        assertThrows(InsufficientBloodStockException.class, () -> {
            inventoryService.deductStockForRequest(hospital.getId(), BloodGroup.O_NEG, BloodComponentType.PLASMA, currentStock + 999);
        });
    }

    @Test
    @DisplayName("Should adjust units up and down successfully")
    void testAdjustStockUnits() {
        HospitalProfile hospital = hospitalService.getVerifiedHospitals().get(0);
        StockUpdateDto dto = StockUpdateDto.builder()
                .bloodGroup(BloodGroup.B_POS)
                .componentType(BloodComponentType.RBC)
                .unitsAvailable(20)
                .batchNumber("TEST-ADJUST-BATCH-01")
                .expiryDate(LocalDate.now().plusDays(35))
                .build();
        BloodInventory saved = inventoryService.addOrUpdateStock(hospital.getId(), dto, "TEST_RUNNER");

        BloodInventory adjusted = inventoryService.adjustStockUnits(saved.getId(), -5, "Damaged unit discarded", "TEST_RUNNER");
        assertEquals(15, adjusted.getUnitsAvailable());

        BloodInventory increased = inventoryService.adjustStockUnits(saved.getId(), 10, "Emergency replenishment", "TEST_RUNNER");
        assertEquals(25, increased.getUnitsAvailable());
    }
}
