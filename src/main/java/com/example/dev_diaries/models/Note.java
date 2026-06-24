package com.example.dev_diaries.models;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "notes")
public record Note(
        @Id 
        @GeneratedValue(strategy = GenerationType.UUID) 
        UUID id,
        String title,
        @Column(columnDefinition = "TEXT") 
        String content,
        Format format,
        LocalDateTime createdAt,
        @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE }) 
        @JoinTable(
            name = "note_tags", 
            joinColumns = @JoinColumn(name = "note_id"), 
            inverseJoinColumns = @JoinColumn(name = "tag_id")) 
        Set<Tag> tags) {


}
