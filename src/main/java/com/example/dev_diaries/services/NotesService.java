package com.example.dev_diaries.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dev_diaries.models.Note;
import com.example.dev_diaries.models.Tag;
import com.example.dev_diaries.repositories.NotesRepository;
import com.example.dev_diaries.repositories.TagsRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class NotesService {
    TaggingService taggingService;
    NotesRepository notesRepository;
    TagsRepository tagsRepository;

    public NotesService(TaggingService taggingService, NotesRepository notesRepository, TagsRepository tagsRepository) {
        this.taggingService = taggingService;
        this.notesRepository = notesRepository;
        this.tagsRepository = tagsRepository;
    }

    public Note createNote(Note note){
        Set<String> detectedTags = taggingService.extractTagsFromContent(note.getContent());
        
        Set<Tag> tags = detectedTags.stream().map(tagname -> tagsRepository.findByNameIgnoreCase(tagname).orElseGet(() -> tagsRepository.save(new Tag(tagname)))).collect(Collectors.toSet());

        note.setTags(tags);
        return notesRepository.save(note);
    }

    public List<Note> getAllNotes() {
        return notesRepository.findAll();
    }

    public Optional<Note> getNoteBYId(UUID id) {
        return notesRepository.findById(id);
    }

    public Note updateNote(UUID id, Note newNote){
        Note oldNote = notesRepository.findById(id).orElseThrow();

        oldNote.setTitle(newNote.getTitle());
        oldNote.setContent(newNote.getContent());
        oldNote.setFormat(newNote.getFormat());
        
        Set<String> extractedTags = taggingService.extractTagsFromContent(newNote.getContent());

        Set<Tag> tags = extractedTags.stream().map((tagString) -> tagsRepository.findByNameIgnoreCase(tagString).orElseGet(() -> tagsRepository.save(new Tag(tagString)))).collect(Collectors.toSet());

        oldNote.setTags(tags);

        return notesRepository.save(oldNote);
    }
}
