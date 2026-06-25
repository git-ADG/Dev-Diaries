package com.example.dev_diaries.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev_diaries.models.Note;
import com.example.dev_diaries.services.NotesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/notes")
public class NotesController {
    NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @GetMapping("/")
    public List<Note> getAllNotes() {
        return notesService.getAllNotes();
    }

    @GetMapping("/{id}")
    public Optional<Note> getNoteById(@PathVariable UUID id) {
        return notesService.getNoteBYId(id);
    }

    @PostMapping("/")
    public Note createNote(@RequestBody Note note){
        return notesService.createNote(note);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable UUID id, @RequestBody Note note){
        return notesService.updateNote(id, note);
    }
}
