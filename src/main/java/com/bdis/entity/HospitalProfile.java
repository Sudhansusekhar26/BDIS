package com.bdis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "hospital_profiles",
    indexes = {
        @Index(name = "idx_hospital_verification", columnList = "is_verified"),
        @Index(name = "idx_hospital_license", columnList = "license_number")
    }
)
public class HospitalProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "hospital_name", nullable = false, length = 200)
    private String hospitalName;

    @Column(name = "license_number", nullable = false, unique = true, length = 100)
    private String licenseNumber;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "contact_person", nullable = false, length = 150)
    private String contactPerson;

    @Column(name = "emergency_helpline", nullable = false, length = 50)
    private String emergencyHelpline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id", nullable = false)
    private LocationState state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id", nullable = false)
    private LocationDistrict district;

    @Column(nullable = false, length = 300)
    private String address;

    @Column(name = "total_beds")
    private Integer totalBeds = 100;

    @JsonIgnore
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BloodInventory> inventories = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY)
    private List<BloodRequest> bloodRequests = new ArrayList<>();

    public HospitalProfile() {}

    public HospitalProfile(Long id, User user, String hospitalName, String licenseNumber, boolean isVerified,
                           String contactPerson, String emergencyHelpline, LocationState state, LocationDistrict district,
                           String address, Integer totalBeds, List<BloodInventory> inventories, List<BloodRequest> bloodRequests) {
        this.id = id;
        this.user = user;
        this.hospitalName = hospitalName;
        this.licenseNumber = licenseNumber;
        this.isVerified = isVerified;
        this.contactPerson = contactPerson;
        this.emergencyHelpline = emergencyHelpline;
        this.state = state;
        this.district = district;
        this.address = address;
        this.totalBeds = totalBeds != null ? totalBeds : 100;
        this.inventories = inventories != null ? inventories : new ArrayList<>();
        this.bloodRequests = bloodRequests != null ? bloodRequests : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getEmergencyHelpline() { return emergencyHelpline; }
    public void setEmergencyHelpline(String emergencyHelpline) { this.emergencyHelpline = emergencyHelpline; }

    public LocationState getState() { return state; }
    public void setState(LocationState state) { this.state = state; }

    public LocationDistrict getDistrict() { return district; }
    public void setDistrict(LocationDistrict district) { this.district = district; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getTotalBeds() { return totalBeds; }
    public void setTotalBeds(Integer totalBeds) { this.totalBeds = totalBeds; }

    public List<BloodInventory> getInventories() { return inventories; }
    public void setInventories(List<BloodInventory> inventories) { this.inventories = inventories; }

    public List<BloodRequest> getBloodRequests() { return bloodRequests; }
    public void setBloodRequests(List<BloodRequest> bloodRequests) { this.bloodRequests = bloodRequests; }

    public static HospitalProfileBuilder builder() { return new HospitalProfileBuilder(); }

    public static class HospitalProfileBuilder {
        private Long id;
        private User user;
        private String hospitalName;
        private String licenseNumber;
        private boolean isVerified = false;
        private String contactPerson;
        private String emergencyHelpline;
        private LocationState state;
        private LocationDistrict district;
        private String address;
        private Integer totalBeds = 100;
        private List<BloodInventory> inventories = new ArrayList<>();
        private List<BloodRequest> bloodRequests = new ArrayList<>();

        public HospitalProfileBuilder id(Long id) { this.id = id; return this; }
        public HospitalProfileBuilder user(User user) { this.user = user; return this; }
        public HospitalProfileBuilder hospitalName(String hospitalName) { this.hospitalName = hospitalName; return this; }
        public HospitalProfileBuilder licenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; return this; }
        public HospitalProfileBuilder isVerified(boolean isVerified) { this.isVerified = isVerified; return this; }
        public HospitalProfileBuilder contactPerson(String contactPerson) { this.contactPerson = contactPerson; return this; }
        public HospitalProfileBuilder emergencyHelpline(String emergencyHelpline) { this.emergencyHelpline = emergencyHelpline; return this; }
        public HospitalProfileBuilder state(LocationState state) { this.state = state; return this; }
        public HospitalProfileBuilder district(LocationDistrict district) { this.district = district; return this; }
        public HospitalProfileBuilder address(String address) { this.address = address; return this; }
        public HospitalProfileBuilder totalBeds(Integer totalBeds) { this.totalBeds = totalBeds; return this; }
        public HospitalProfileBuilder inventories(List<BloodInventory> inventories) { this.inventories = inventories; return this; }
        public HospitalProfileBuilder bloodRequests(List<BloodRequest> bloodRequests) { this.bloodRequests = bloodRequests; return this; }

        public HospitalProfile build() {
            return new HospitalProfile(id, user, hospitalName, licenseNumber, isVerified, contactPerson, emergencyHelpline, state, district, address, totalBeds, inventories, bloodRequests);
        }
    }
}
