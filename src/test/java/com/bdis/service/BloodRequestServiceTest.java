package com.bdis.service;

import com.bdis.dto.BloodRequestCreateDto;
import com.bdis.dto.DeliveryUpdateDto;
import com.bdis.entity.BloodRequest;
import com.bdis.entity.HospitalProfile;
import com.bdis.entity.User;
import com.bdis.enums.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BloodRequestServiceTest {

    @Autowired
    private BloodRequestService requestService;

    @Autowired
    private UserService userService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private BloodInventoryService inventoryService;

    @Test
    @DisplayName("Should create, accept, dispatch and deliver blood requisition lifecycle")
    void testBloodRequestLifecycle() {
        User user = userService.getUserByEmail("donor@gmail.com");
        HospitalProfile hospital = hospitalService.getVerifiedHospitals().get(0);

        // 1. Create Requisition
        BloodRequestCreateDto createDto = BloodRequestCreateDto.builder()
                .hospitalId(hospital.getId())
                .patientName("Amitav Roy")
                .admittedHospitalName("City Trauma Center, Ward 2")
                .bloodGroup(BloodGroup.O_POS)
                .componentType(BloodComponentType.WHOLE_BLOOD)
                .unitsRequested(2)
                .urgencyLevel(UrgencyLevel.EMERGENCY)
                .deliveryAddress("City Trauma Center, Emergency Reception")
                .prescriptionNotes("Severe acute blood loss in vehicular accident.")
                .build();

        BloodRequest created = requestService.createRequest(user.getId(), createDto);
        assertNotNull(created.getId());
        assertEquals(RequestStatus.PENDING, created.getStatus());
        assertEquals("Amitav Roy", created.getPatientName());
        assertEquals(2, created.getUnitsRequested());

        // 2. Accept Requisition
        int initialStock = inventoryService.getAvailableStockCount(hospital.getId(), BloodGroup.O_POS, BloodComponentType.WHOLE_BLOOD);
        BloodRequest accepted = requestService.acceptRequest(created.getId(), "apollo@hospital.org");
        assertEquals(RequestStatus.ACCEPTED, accepted.getStatus());
        assertNotNull(accepted.getDelivery());

        int postAcceptStock = inventoryService.getAvailableStockCount(hospital.getId(), BloodGroup.O_POS, BloodComponentType.WHOLE_BLOOD);
        assertEquals(initialStock - 2, postAcceptStock);

        // 3. Dispatch Rider
        DeliveryUpdateDto deliveryDto = DeliveryUpdateDto.builder()
                .deliveryPartnerName("Dedicated MediVan - Rakesh Sahoo")
                .deliveryContact("+91-9988776655")
                .vehicleNumber("OD-02-ZZ-9999")
                .trackingNotes("Packed with thermal ice packs.")
                .currentStatus(DeliveryStatus.IN_TRANSIT)
                .build();

        BloodRequest dispatched = requestService.updateDeliveryInfo(created.getId(), deliveryDto, "apollo@hospital.org");
        assertEquals(RequestStatus.DISPATCHED, dispatched.getStatus());
        assertNotNull(dispatched.getDelivery().getDispatchedAt());

        // 4. Mark Delivered
        BloodRequest delivered = requestService.updateStatus(created.getId(), RequestStatus.DELIVERED, "apollo@hospital.org");
        assertEquals(RequestStatus.DELIVERED, delivered.getStatus());
        assertNotNull(delivered.getDelivery().getDeliveredAt());
    }

    @Test
    @DisplayName("Should reject blood request with reason")
    void testRejectRequest() {
        User user = userService.getUserByEmail("donor@gmail.com");
        HospitalProfile hospital = hospitalService.getVerifiedHospitals().get(0);

        BloodRequestCreateDto createDto = BloodRequestCreateDto.builder()
                .hospitalId(hospital.getId())
                .patientName("Test Reject Patient")
                .admittedHospitalName("Test Hospital")
                .bloodGroup(BloodGroup.AB_POS)
                .componentType(BloodComponentType.PLASMA)
                .unitsRequested(1)
                .urgencyLevel(UrgencyLevel.NORMAL)
                .deliveryAddress("Test Address")
                .build();

        BloodRequest created = requestService.createRequest(user.getId(), createDto);
        BloodRequest rejected = requestService.rejectRequest(created.getId(), "No compatible plasma available", "apollo@hospital.org");

        assertEquals(RequestStatus.REJECTED, rejected.getStatus());
        assertEquals("No compatible plasma available", rejected.getRejectionReason());
    }
}
