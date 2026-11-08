package com.bdis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class HospitalRegistrationDto {

    @NotBlank(message = "Hospital name is required")
    private String hospitalName;

    @NotBlank(message = "Medical license number is required")
    private String licenseNumber;

    @NotBlank(message = "Contact person name is required")
    private String contactPerson;

    @NotBlank(message = "Official email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Contact phone number is required")
    private String phone;

    @NotBlank(message = "Emergency 24x7 helpline number is required")
    private String emergencyHelpline;

    @NotNull(message = "State is required")
    private Long stateId;

    @NotNull(message = "District is required")
    private Long districtId;

    @NotBlank(message = "Hospital complete address is required")
    private String address;

    private Integer totalBeds = 100;

    public HospitalRegistrationDto() {}

    public HospitalRegistrationDto(String hospitalName, String licenseNumber, String contactPerson, String email, String password, String phone, String emergencyHelpline, Long stateId, Long districtId, String address, Integer totalBeds) {
        this.hospitalName = hospitalName;
        this.licenseNumber = licenseNumber;
        this.contactPerson = contactPerson;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.emergencyHelpline = emergencyHelpline;
        this.stateId = stateId;
        this.districtId = districtId;
        this.address = address;
        this.totalBeds = totalBeds != null ? totalBeds : 100;
    }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmergencyHelpline() { return emergencyHelpline; }
    public void setEmergencyHelpline(String emergencyHelpline) { this.emergencyHelpline = emergencyHelpline; }

    public Long getStateId() { return stateId; }
    public void setStateId(Long stateId) { this.stateId = stateId; }

    public Long getDistrictId() { return districtId; }
    public void setDistrictId(Long districtId) { this.districtId = districtId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getTotalBeds() { return totalBeds; }
    public void setTotalBeds(Integer totalBeds) { this.totalBeds = totalBeds; }

    public static HospitalRegistrationDtoBuilder builder() { return new HospitalRegistrationDtoBuilder(); }

    public static class HospitalRegistrationDtoBuilder {
        private String hospitalName;
        private String licenseNumber;
        private String contactPerson;
        private String email;
        private String password;
        private String phone;
        private String emergencyHelpline;
        private Long stateId;
        private Long districtId;
        private String address;
        private Integer totalBeds = 100;

        public HospitalRegistrationDtoBuilder hospitalName(String hospitalName) { this.hospitalName = hospitalName; return this; }
        public HospitalRegistrationDtoBuilder licenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; return this; }
        public HospitalRegistrationDtoBuilder contactPerson(String contactPerson) { this.contactPerson = contactPerson; return this; }
        public HospitalRegistrationDtoBuilder email(String email) { this.email = email; return this; }
        public HospitalRegistrationDtoBuilder password(String password) { this.password = password; return this; }
        public HospitalRegistrationDtoBuilder phone(String phone) { this.phone = phone; return this; }
        public HospitalRegistrationDtoBuilder emergencyHelpline(String emergencyHelpline) { this.emergencyHelpline = emergencyHelpline; return this; }
        public HospitalRegistrationDtoBuilder stateId(Long stateId) { this.stateId = stateId; return this; }
        public HospitalRegistrationDtoBuilder districtId(Long districtId) { this.districtId = districtId; return this; }
        public HospitalRegistrationDtoBuilder address(String address) { this.address = address; return this; }
        public HospitalRegistrationDtoBuilder totalBeds(Integer totalBeds) { this.totalBeds = totalBeds; return this; }

        public HospitalRegistrationDto build() {
            return new HospitalRegistrationDto(hospitalName, licenseNumber, contactPerson, email, password, phone, emergencyHelpline, stateId, districtId, address, totalBeds);
        }
    }
}
