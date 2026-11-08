package com.bdis.entity;

import com.bdis.enums.BloodGroup;
import com.bdis.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_users_email", columnList = "email"),
        @Index(name = "idx_users_role", columnList = "role")
    }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", length = 20)
    private BloodGroup bloodGroup;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    private LocationState state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private LocationDistrict district;

    @Column(length = 300)
    private String address;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = true;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private HospitalProfile hospitalProfile;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User() {}

    public User(Long id, String name, String email, String password, String phone, Role role,
                BloodGroup bloodGroup, LocationState state, LocationDistrict district, String address,
                boolean isEnabled, HospitalProfile hospitalProfile, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.bloodGroup = bloodGroup;
        this.state = state;
        this.district = district;
        this.address = address;
        this.isEnabled = isEnabled;
        this.hospitalProfile = hospitalProfile;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }

    public LocationState getState() { return state; }
    public void setState(LocationState state) { this.state = state; }

    public LocationDistrict getDistrict() { return district; }
    public void setDistrict(LocationDistrict district) { this.district = district; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean enabled) { isEnabled = enabled; }

    public HospitalProfile getHospitalProfile() { return hospitalProfile; }
    public void setHospitalProfile(HospitalProfile hospitalProfile) { this.hospitalProfile = hospitalProfile; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static UserBuilder builder() { return new UserBuilder(); }

    public static class UserBuilder {
        private Long id;
        private String name;
        private String email;
        private String password;
        private String phone;
        private Role role;
        private BloodGroup bloodGroup;
        private LocationState state;
        private LocationDistrict district;
        private String address;
        private boolean isEnabled = true;
        private HospitalProfile hospitalProfile;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public UserBuilder id(Long id) { this.id = id; return this; }
        public UserBuilder name(String name) { this.name = name; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder phone(String phone) { this.phone = phone; return this; }
        public UserBuilder role(Role role) { this.role = role; return this; }
        public UserBuilder bloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public UserBuilder state(LocationState state) { this.state = state; return this; }
        public UserBuilder district(LocationDistrict district) { this.district = district; return this; }
        public UserBuilder address(String address) { this.address = address; return this; }
        public UserBuilder isEnabled(boolean isEnabled) { this.isEnabled = isEnabled; return this; }
        public UserBuilder hospitalProfile(HospitalProfile hospitalProfile) { this.hospitalProfile = hospitalProfile; return this; }
        public UserBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public UserBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public User build() {
            return new User(id, name, email, password, phone, role, bloodGroup, state, district, address, isEnabled, hospitalProfile, createdAt, updatedAt);
        }
    }
}
