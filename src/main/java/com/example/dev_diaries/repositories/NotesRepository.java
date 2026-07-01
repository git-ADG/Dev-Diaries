package com.example.dev_diaries.repositories;

import java.util.UUID;
import com.example.dev_diaries.models.Note;
import com.example.dev_diaries.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;


public interface NotesRepository extends JpaRepository<Note, UUID>, JpaSpecificationExecutor<Note> {
    
    List<Note> findByUser(User user);

    List<Note> findByUser_Id(UUID userId);
}
