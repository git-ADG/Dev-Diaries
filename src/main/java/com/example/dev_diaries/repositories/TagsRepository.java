package com.example.dev_diaries.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dev_diaries.models.Tag;

public interface TagsRepository extends JpaRepository<Tag, UUID>{
    Optional<Tag> findByNameIgnoreCase(String name);
}
