package com.bdis.service;

import com.bdis.dto.UserRegistrationDto;
import com.bdis.entity.DonorPledge;
import com.bdis.entity.User;
import com.bdis.enums.Role;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    User registerUser(UserRegistrationDto dto);

    User getUserByEmail(String email);

    User getUserById(Long id);

    boolean existsByEmail(String email);

    List<User> getAllUsers();

    List<User> getUsersByRole(Role role);

    DonorPledge createDonorPledge(Long userId, Long hospitalId, LocalDate pledgedDate, String notes);

    List<DonorPledge> getDonorPledges(Long userId);

    void updateUserStatus(Long userId, boolean isEnabled);
}
