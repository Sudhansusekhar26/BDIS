package com.bdis.dto;

import com.bdis.enums.BloodGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    private String phone;

    private BloodGroup bloodGroup;

    @NotNull(message = "State is required")
    private Long stateId;

    @NotNull(message = "District is required")
    private Long districtId;

    @NotBlank(message = "Residential address is required")
    private String address;

    public UserRegistrationDto() {}

    public UserRegistrationDto(String name, String email, String password, String phone, BloodGroup bloodGroup, Long stateId, Long districtId, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.bloodGroup = bloodGroup;
        this.stateId = stateId;
        this.districtId = districtId;
        this.address = address;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }

    public Long getStateId() { return stateId; }
    public void setStateId(Long stateId) { this.stateId = stateId; }

    public Long getDistrictId() { return districtId; }
    public void setDistrictId(Long districtId) { this.districtId = districtId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public static UserRegistrationDtoBuilder builder() { return new UserRegistrationDtoBuilder(); }

    public static class UserRegistrationDtoBuilder {
        private String name;
        private String email;
        private String password;
        private String phone;
        private BloodGroup bloodGroup;
        private Long stateId;
        private Long districtId;
        private String address;

        public UserRegistrationDtoBuilder name(String name) { this.name = name; return this; }
        public UserRegistrationDtoBuilder email(String email) { this.email = email; return this; }
        public UserRegistrationDtoBuilder password(String password) { this.password = password; return this; }
        public UserRegistrationDtoBuilder phone(String phone) { this.phone = phone; return this; }
        public UserRegistrationDtoBuilder bloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public UserRegistrationDtoBuilder stateId(Long stateId) { this.stateId = stateId; return this; }
        public UserRegistrationDtoBuilder districtId(Long districtId) { this.districtId = districtId; return this; }
        public UserRegistrationDtoBuilder address(String address) { this.address = address; return this; }

        public UserRegistrationDto build() {
            return new UserRegistrationDto(name, email, password, phone, bloodGroup, stateId, districtId, address);
        }
    }
}
