package com.example.dev_diaries.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags")
public record Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id,
    @Column(nullable = false, unique = true)
    String name
) {

};
