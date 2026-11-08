package com.bdis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(
    name = "location_districts",
    uniqueConstraints = @UniqueConstraint(columnNames = {"state_id", "name"})
)
public class LocationDistrict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", nullable = false)
    @JsonIgnore
    private LocationState state;

    public LocationDistrict() {}

    public LocationDistrict(String name, LocationState state) {
        this.name = name;
        this.state = state;
    }

    public LocationDistrict(Long id, String name, LocationState state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocationState getState() { return state; }
    public void setState(LocationState state) { this.state = state; }

    public static LocationDistrictBuilder builder() { return new LocationDistrictBuilder(); }

    public static class LocationDistrictBuilder {
        private Long id;
        private String name;
        private LocationState state;

        public LocationDistrictBuilder id(Long id) { this.id = id; return this; }
        public LocationDistrictBuilder name(String name) { this.name = name; return this; }
        public LocationDistrictBuilder state(LocationState state) { this.state = state; return this; }

        public LocationDistrict build() {
            return new LocationDistrict(id, name, state);
        }
    }
}
