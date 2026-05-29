package com.github.ezequielarroyo.postservice.repositories;

import com.github.ezequielarroyo.postservice.entities.UserSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IUserSnapshotRepository extends JpaRepository<UserSnapshot, Long> {
    Optional<UserSnapshot> findByUuid(UUID uuid);
    Boolean existsByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);
}
