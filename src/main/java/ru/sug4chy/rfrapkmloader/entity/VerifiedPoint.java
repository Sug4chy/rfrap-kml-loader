package ru.sug4chy.rfrapkmloader.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "verified_point")
public class VerifiedPoint {

    @Column(name = "point_type", nullable = false)
    private final String pointType = "KilometerPole";

    @Column(name = "Discriminator", nullable = false)
    private final String discriminator = "KilometerPole";

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "road_id")
    private Road road;

    @Column(name = "point_name", nullable = false)
    private String pointName;

    @Column(name = "coordinates_latitude", nullable = false)
    private double latitude;

    @Column(name = "coordinates_longitude", nullable = false)
    private double longitude;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at_utc", nullable = false)
    private OffsetDateTime createdAtUtc = OffsetDateTime.now();

    @Column(name = "lastly_updated_at_utc", nullable = false)
    private OffsetDateTime lastlyUpdatedAtUtc = OffsetDateTime.now();

}