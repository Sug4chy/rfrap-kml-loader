package ru.sug4chy.rfrapkmloader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sug4chy.rfrapkmloader.entity.Road;

import java.util.UUID;

@Repository
public interface RoadRepository extends JpaRepository<Road, UUID> {
}