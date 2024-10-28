package ru.sug4chy.rfrapkmloader.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sug4chy.rfrapkmloader.entity.VerifiedPoint;

import java.util.UUID;

@Repository
public interface VerifiedPointRepository extends CrudRepository<VerifiedPoint, UUID> {
    int countByPointType(String discriminator);
}