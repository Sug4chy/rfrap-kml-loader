package ru.sug4chy.rfrapkmloader.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "region")
public class Region {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "region_name", nullable = false)
    private String regionName;

    @Column(name = "created_at_utc", nullable = false)
    private OffsetDateTime createdAtUtc;

    @Column(name = "lastly_updated_at_utc", nullable = false)
    private OffsetDateTime lastlyUpdatedAtUtc;

    @ManyToMany
    @JoinTable(
            name = "region_road",
            schema = "public",
            joinColumns = @JoinColumn(name = "region_id"),
            inverseJoinColumns = @JoinColumn(name = "road_id")
    )
    private Set<Road> roads = Collections.emptySet();
}