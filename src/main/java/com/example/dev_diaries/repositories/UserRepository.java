package com.example.dev_diaries.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dev_diaries.models.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional <User> findByEmail(String email);
}
