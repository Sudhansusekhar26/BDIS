package com.bdis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "location_states")
public class LocationState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @JsonIgnore
    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LocationDistrict> districts = new ArrayList<>();

    public LocationState() {}

    public LocationState(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public LocationState(Long id, String name, String code, List<LocationDistrict> districts) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.districts = districts != null ? districts : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public List<LocationDistrict> getDistricts() { return districts; }
    public void setDistricts(List<LocationDistrict> districts) { this.districts = districts; }

    public static LocationStateBuilder builder() { return new LocationStateBuilder(); }

    public static class LocationStateBuilder {
        private Long id;
        private String name;
        private String code;
        private List<LocationDistrict> districts = new ArrayList<>();

        public LocationStateBuilder id(Long id) { this.id = id; return this; }
        public LocationStateBuilder name(String name) { this.name = name; return this; }
        public LocationStateBuilder code(String code) { this.code = code; return this; }
        public LocationStateBuilder districts(List<LocationDistrict> districts) { this.districts = districts; return this; }

        public LocationState build() {
            return new LocationState(id, name, code, districts);
        }
    }
}
