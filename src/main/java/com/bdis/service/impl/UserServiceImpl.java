package com.bdis.service.impl;

import com.bdis.dto.UserRegistrationDto;
import com.bdis.entity.DonorPledge;
import com.bdis.entity.HospitalProfile;
import com.bdis.entity.LocationDistrict;
import com.bdis.entity.LocationState;
import com.bdis.entity.User;
import com.bdis.enums.Role;
import com.bdis.exception.InvalidOperationException;
import com.bdis.exception.ResourceNotFoundException;
import com.bdis.repository.DonorPledgeRepository;
import com.bdis.repository.HospitalProfileRepository;
import com.bdis.repository.UserRepository;
import com.bdis.service.AuditLogService;
import com.bdis.service.LocationService;
import com.bdis.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final HospitalProfileRepository hospitalProfileRepository;
    private final DonorPledgeRepository donorPledgeRepository;
    private final LocationService locationService;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    public UserServiceImpl(UserRepository userRepository,
                           HospitalProfileRepository hospitalProfileRepository,
                           DonorPledgeRepository donorPledgeRepository,
                           LocationService locationService,
                           PasswordEncoder passwordEncoder,
                           AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.hospitalProfileRepository = hospitalProfileRepository;
        this.donorPledgeRepository = donorPledgeRepository;
        this.locationService = locationService;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public User registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail().trim().toLowerCase())) {
            throw new InvalidOperationException("An account with email " + dto.getEmail() + " already exists.");
        }

        LocationState state = locationService.getStateById(dto.getStateId());
        LocationDistrict district = locationService.getDistrictById(dto.getDistrictId());

        User user = User.builder()
                .name(dto.getName().trim())
                .email(dto.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone().trim())
                .role(Role.ROLE_USER)
                .bloodGroup(dto.getBloodGroup())
                .state(state)
                .district(district)
                .address(dto.getAddress().trim())
                .isEnabled(true)
                .build();

        User savedUser = userRepository.save(user);
        auditLogService.log("USER_REGISTER", savedUser.getEmail(), "User", String.valueOf(savedUser.getId()), "Registered new user/donor");
        return savedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    @Transactional
    public DonorPledge createDonorPledge(Long userId, Long hospitalId, LocalDate pledgedDate, String notes) {
        User donor = getUserById(userId);
        HospitalProfile hospital = null;
        if (hospitalId != null) {
            hospital = hospitalProfileRepository.findById(hospitalId).orElse(null);
        }

        DonorPledge pledge = DonorPledge.builder()
                .donor(donor)
                .hospital(hospital)
                .pledgedDate(pledgedDate != null ? pledgedDate : LocalDate.now().plusDays(1))
                .status("PLEDGED")
                .unitsDonated(1)
                .notes(notes)
                .build();

        DonorPledge saved = donorPledgeRepository.save(pledge);
        auditLogService.log("DONOR_PLEDGE", donor.getEmail(), "DonorPledge", String.valueOf(saved.getId()), "Volunteered blood donation pledge");
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonorPledge> getDonorPledges(Long userId) {
        return donorPledgeRepository.findByDonor_IdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, boolean isEnabled) {
        User user = getUserById(userId);
        user.setEnabled(isEnabled);
        userRepository.save(user);
        auditLogService.log("USER_STATUS_CHANGE", "ADMIN", "User", String.valueOf(userId), "Set enabled=" + isEnabled);
    }
}
