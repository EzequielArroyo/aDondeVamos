package com.github.ezequielarroyo.postservice.repositories;

import com.github.ezequielarroyo.postservice.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findByUuid(UUID uuid);
}


