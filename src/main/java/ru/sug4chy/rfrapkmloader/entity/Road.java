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
@Table(name = "road")
public class Road {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToMany(mappedBy = "roads")
    private Set<Region> regions;

    @Column(name = "road_name", nullable = false)
    private String roadName;

    @Column(name = "created_at_utc", nullable = false)
    private OffsetDateTime createdAtUtc = OffsetDateTime.MIN;

    @Column(name = "lastly_updated_at_utc", nullable = false)
    private OffsetDateTime lastlyUpdatedAtUtc = OffsetDateTime.MIN;

    @OneToMany(mappedBy = "road")
    private Set<VerifiedPoint> verifiedPoints = Collections.emptySet();

}