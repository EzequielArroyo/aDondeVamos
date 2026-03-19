package com.github.ezequielarroyo.postservice.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Embeddable @NoArgsConstructor @Getter @Setter
public class Location {
    private Double latitude;
    private Double longitude;

    private Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public static Location create(Double latitude, Double longitude) {
        return new Location(latitude, longitude);
    }
}
