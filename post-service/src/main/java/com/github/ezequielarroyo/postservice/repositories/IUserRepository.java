package com.github.ezequielarroyo.postservice.repositories;

import com.github.ezequielarroyo.postservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(UUID uuid);
}
