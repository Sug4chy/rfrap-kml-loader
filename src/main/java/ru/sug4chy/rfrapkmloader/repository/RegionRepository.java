package ru.sug4chy.rfrapkmloader.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sug4chy.rfrapkmloader.entity.Region;

import java.util.UUID;

@Repository
public interface RegionRepository extends CrudRepository<Region, UUID> {
}