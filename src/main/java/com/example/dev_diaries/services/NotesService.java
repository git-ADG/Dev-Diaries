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
        Set<String> detectedTags = taggingService.extractTagsFromContent(note.content());
        
        Set<Tag> tags = detectedTags.stream().map(tagname -> tagsRepository.findByNameIgnoreCase(tagname).orElseGet(() -> tagsRepository.save(new Tag(null, tagname)))).collect(Collectors.toSet());

        Note newNote = new Note(note.id(), note.title(), note.content(), note.format(), note.createdAt(), tags);

        return notesRepository.save(newNote);
    }

    public List<Note> getAllNotes() {
        return notesRepository.findAll();
    }

    public Optional<Note> getNoteBYId(UUID id) {
        return notesRepository.findById(id);
    }
}
