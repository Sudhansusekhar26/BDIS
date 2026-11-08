package com.bdis.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "donor_pledges")
public class DonorPledge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hospital_id")
    private HospitalProfile hospital;

    @Column(name = "pledged_date", nullable = false)
    private LocalDate pledgedDate;

    @Column(nullable = false, length = 30)
    private String status = "PLEDGED";

    @Column(name = "units_donated")
    private int unitsDonated = 1;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public DonorPledge() {}

    public DonorPledge(Long id, User donor, HospitalProfile hospital, LocalDate pledgedDate, String status, int unitsDonated, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.donor = donor;
        this.hospital = hospital;
        this.pledgedDate = pledgedDate;
        this.status = status != null ? status : "PLEDGED";
        this.unitsDonated = unitsDonated;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getDonor() { return donor; }
    public void setDonor(User donor) { this.donor = donor; }

    public HospitalProfile getHospital() { return hospital; }
    public void setHospital(HospitalProfile hospital) { this.hospital = hospital; }

    public LocalDate getPledgedDate() { return pledgedDate; }
    public void setPledgedDate(LocalDate pledgedDate) { this.pledgedDate = pledgedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getUnitsDonated() { return unitsDonated; }
    public void setUnitsDonated(int unitsDonated) { this.unitsDonated = unitsDonated; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static DonorPledgeBuilder builder() { return new DonorPledgeBuilder(); }

    public static class DonorPledgeBuilder {
        private Long id;
        private User donor;
        private HospitalProfile hospital;
        private LocalDate pledgedDate;
        private String status = "PLEDGED";
        private int unitsDonated = 1;
        private String notes;
        private LocalDateTime createdAt;

        public DonorPledgeBuilder id(Long id) { this.id = id; return this; }
        public DonorPledgeBuilder donor(User donor) { this.donor = donor; return this; }
        public DonorPledgeBuilder hospital(HospitalProfile hospital) { this.hospital = hospital; return this; }
        public DonorPledgeBuilder pledgedDate(LocalDate pledgedDate) { this.pledgedDate = pledgedDate; return this; }
        public DonorPledgeBuilder status(String status) { this.status = status; return this; }
        public DonorPledgeBuilder unitsDonated(int unitsDonated) { this.unitsDonated = unitsDonated; return this; }
        public DonorPledgeBuilder notes(String notes) { this.notes = notes; return this; }
        public DonorPledgeBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public DonorPledge build() {
            return new DonorPledge(id, donor, hospital, pledgedDate, status, unitsDonated, notes, createdAt);
        }
    }
}
