package com.bdis.service.impl;

import com.bdis.dto.HospitalRegistrationDto;
import com.bdis.entity.HospitalProfile;
import com.bdis.entity.LocationDistrict;
import com.bdis.entity.LocationState;
import com.bdis.entity.User;
import com.bdis.enums.Role;
import com.bdis.exception.InvalidOperationException;
import com.bdis.exception.ResourceNotFoundException;
import com.bdis.repository.HospitalProfileRepository;
import com.bdis.repository.UserRepository;
import com.bdis.service.AuditLogService;
import com.bdis.service.HospitalService;
import com.bdis.service.LocationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HospitalServiceImpl implements HospitalService {

    private final HospitalProfileRepository hospitalProfileRepository;
    private final UserRepository userRepository;
    private final LocationService locationService;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    public HospitalServiceImpl(HospitalProfileRepository hospitalProfileRepository,
                               UserRepository userRepository,
                               LocationService locationService,
                               PasswordEncoder passwordEncoder,
                               AuditLogService auditLogService) {
        this.hospitalProfileRepository = hospitalProfileRepository;
        this.userRepository = userRepository;
        this.locationService = locationService;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public HospitalProfile registerHospital(HospitalRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail().trim().toLowerCase())) {
            throw new InvalidOperationException("An account with email " + dto.getEmail() + " already exists.");
        }
        if (hospitalProfileRepository.existsByLicenseNumber(dto.getLicenseNumber().trim())) {
            throw new InvalidOperationException("A hospital with license number " + dto.getLicenseNumber() + " is already registered.");
        }

        LocationState state = locationService.getStateById(dto.getStateId());
        LocationDistrict district = locationService.getDistrictById(dto.getDistrictId());

        User user = User.builder()
                .name(dto.getHospitalName().trim())
                .email(dto.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone().trim())
                .role(Role.ROLE_HOSPITAL)
                .state(state)
                .district(district)
                .address(dto.getAddress().trim())
                .isEnabled(true)
                .build();

        User savedUser = userRepository.save(user);

        HospitalProfile profile = HospitalProfile.builder()
                .user(savedUser)
                .hospitalName(dto.getHospitalName().trim())
                .licenseNumber(dto.getLicenseNumber().trim())
                .isVerified(false)
                .contactPerson(dto.getContactPerson().trim())
                .emergencyHelpline(dto.getEmergencyHelpline().trim())
                .state(state)
                .district(district)
                .address(dto.getAddress().trim())
                .totalBeds(dto.getTotalBeds() != null ? dto.getTotalBeds() : 100)
                .build();

        HospitalProfile savedHospital = hospitalProfileRepository.save(profile);
        auditLogService.log("HOSPITAL_REGISTER", savedUser.getEmail(), "HospitalProfile", String.valueOf(savedHospital.getId()), "Submitted hospital registration for verification");
        return savedHospital;
    }

    @Override
    @Transactional(readOnly = true)
    public HospitalProfile getHospitalById(Long id) {
        return hospitalProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public HospitalProfile getHospitalByUserId(Long userId) {
        return hospitalProfileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital profile not found for user ID: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public HospitalProfile getHospitalByUserEmail(String email) {
        return hospitalProfileRepository.findByUser_Email(email.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital profile not found for email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HospitalProfile> getAllHospitals() {
        return hospitalProfileRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HospitalProfile> getVerifiedHospitals() {
        return hospitalProfileRepository.findByIsVerified(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HospitalProfile> getPendingHospitals() {
        return hospitalProfileRepository.findByIsVerified(false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HospitalProfile> getVerifiedHospitalsByLocation(Long stateId, Long districtId) {
        return hospitalProfileRepository.findVerifiedByLocation(stateId, districtId);
    }

    @Override
    @Transactional
    public HospitalProfile verifyHospital(Long hospitalId, boolean approved) {
        HospitalProfile hospital = getHospitalById(hospitalId);
        hospital.setVerified(approved);
        HospitalProfile updated = hospitalProfileRepository.save(hospital);
        auditLogService.log("HOSPITAL_VERIFICATION", "ADMIN", "HospitalProfile", String.valueOf(hospitalId), approved ? "Hospital verified and approved" : "Hospital rejected/revoked");
        return updated;
    }
}
