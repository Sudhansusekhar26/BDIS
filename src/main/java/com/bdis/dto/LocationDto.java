package com.bdis.dto;

import java.util.List;

public class LocationDto {

    private Long id;
    private String name;
    private String code;
    private Long stateId;
    private List<LocationDto> districts;

    public LocationDto() {}

    public LocationDto(Long id, String name, String code, Long stateId, List<LocationDto> districts) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.stateId = stateId;
        this.districts = districts;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Long getStateId() { return stateId; }
    public void setStateId(Long stateId) { this.stateId = stateId; }

    public List<LocationDto> getDistricts() { return districts; }
    public void setDistricts(List<LocationDto> districts) { this.districts = districts; }

    public static LocationDto fromState(com.bdis.entity.LocationState state) {
        return LocationDto.builder()
                .id(state.getId())
                .name(state.getName())
                .code(state.getCode())
                .build();
    }

    public static LocationDto fromDistrict(com.bdis.entity.LocationDistrict district) {
        return LocationDto.builder()
                .id(district.getId())
                .name(district.getName())
                .stateId(district.getState() != null ? district.getState().getId() : null)
                .build();
    }

    public static LocationDtoBuilder builder() { return new LocationDtoBuilder(); }

    public static class LocationDtoBuilder {
        private Long id;
        private String name;
        private String code;
        private Long stateId;
        private List<LocationDto> districts;

        public LocationDtoBuilder id(Long id) { this.id = id; return this; }
        public LocationDtoBuilder name(String name) { this.name = name; return this; }
        public LocationDtoBuilder code(String code) { this.code = code; return this; }
        public LocationDtoBuilder stateId(Long stateId) { this.stateId = stateId; return this; }
        public LocationDtoBuilder districts(List<LocationDto> districts) { this.districts = districts; return this; }

        public LocationDto build() {
            return new LocationDto(id, name, code, stateId, districts);
        }
    }
}
