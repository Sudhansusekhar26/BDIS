package com.bdis.controller.api;

import com.bdis.dto.ApiResponse;
import com.bdis.dto.BloodRequestCreateDto;
import com.bdis.dto.BloodRequestResponseDto;
import com.bdis.dto.DeliveryUpdateDto;
import com.bdis.entity.BloodRequest;
import com.bdis.entity.User;
import com.bdis.enums.RequestStatus;
import com.bdis.service.BloodRequestService;
import com.bdis.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
public class BloodRequestApiController {

    private final BloodRequestService requestService;
    private final UserService userService;

    public BloodRequestApiController(BloodRequestService requestService, UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BloodRequestResponseDto>> createRequest(
            @Valid @RequestBody BloodRequestCreateDto dto,
            Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);

        BloodRequest request = requestService.createRequest(user.getId(), dto);
        BloodRequestResponseDto responseDto = requestService.getRequestDtoById(request.getId());

        return ResponseEntity.ok(ApiResponse.ok("Blood request submitted successfully with ID: " + request.getId(), responseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BloodRequestResponseDto>> getRequestById(@PathVariable Long id) {
        BloodRequestResponseDto dto = requestService.getRequestDtoById(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<BloodRequestResponseDto>> acceptRequest(
            @PathVariable Long id,
            Authentication authentication) {
        String performedBy = authentication != null ? authentication.getName() : "HOSPITAL";
        BloodRequest request = requestService.acceptRequest(id, performedBy);
        return ResponseEntity.ok(ApiResponse.ok("Request accepted and stock allocated successfully.", requestService.getRequestDtoById(request.getId())));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<BloodRequestResponseDto>> rejectRequest(
            @PathVariable Long id,
            @RequestParam(value = "reason", required = false) String reason,
            Authentication authentication) {
        String performedBy = authentication != null ? authentication.getName() : "HOSPITAL";
        BloodRequest request = requestService.rejectRequest(id, reason, performedBy);
        return ResponseEntity.ok(ApiResponse.ok("Request rejected.", requestService.getRequestDtoById(request.getId())));
    }

    @PutMapping("/{id}/delivery")
    public ResponseEntity<ApiResponse<BloodRequestResponseDto>> updateDelivery(
            @PathVariable Long id,
            @Valid @RequestBody DeliveryUpdateDto dto,
            Authentication authentication) {
        String performedBy = authentication != null ? authentication.getName() : "HOSPITAL";
        BloodRequest request = requestService.updateDeliveryInfo(id, dto, performedBy);
        return ResponseEntity.ok(ApiResponse.ok("Delivery status updated successfully", requestService.getRequestDtoById(request.getId())));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BloodRequestResponseDto>> updateStatus(
            @PathVariable Long id,
            @RequestParam("status") RequestStatus status,
            Authentication authentication) {
        String performedBy = authentication != null ? authentication.getName() : "ADMIN";
        BloodRequest request = requestService.updateStatus(id, status, performedBy);
        return ResponseEntity.ok(ApiResponse.ok("Status updated to " + status.getLabel(), requestService.getRequestDtoById(request.getId())));
    }
}
