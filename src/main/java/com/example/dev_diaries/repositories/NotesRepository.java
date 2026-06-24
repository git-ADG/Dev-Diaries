package com.example.dev_diaries.repositories;

import java.util.UUID;
import com.example.dev_diaries.models.Note;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotesRepository extends JpaRepository<Note, UUID> {
    
}
