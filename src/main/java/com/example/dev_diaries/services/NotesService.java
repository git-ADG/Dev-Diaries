package com.example.dev_diaries.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.dev_diaries.models.Format;
import com.example.dev_diaries.models.Note;
import com.example.dev_diaries.models.Tag;
import com.example.dev_diaries.models.User;
import com.example.dev_diaries.repositories.NotesRepository;
import com.example.dev_diaries.repositories.TagsRepository;
import com.example.dev_diaries.repositories.UserRepository;
import com.example.dev_diaries.specifications.NotesSpecification;

import jakarta.persistence.EntityNotFoundException;

@Service
public class NotesService {
    TaggingService taggingService;
    NotesRepository notesRepository;
    TagsRepository tagsRepository;
    UserRepository userRepository;

    public NotesService(TaggingService taggingService, NotesRepository notesRepository, TagsRepository tagsRepository, UserRepository userRepository) {
        this.taggingService = taggingService;
        this.notesRepository = notesRepository;
        this.tagsRepository = tagsRepository;
        this.userRepository = userRepository;
    }

    private User getUser(String email){
        return userRepository.findByEmail(email).orElseThrow(
            () -> new EntityNotFoundException("User not found")
        );
    }
    public Note createNote(Note note, String userEmail) {
        User user = getUser(userEmail);
        String text = Stream.of(note.getContent(), note.getTitle())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));

        Set<String> detectedTags = taggingService.extractTagsFromContent(text);

        Set<Tag> tags = detectedTags.stream().map(tagname -> tagsRepository.findByNameIgnoreCase(tagname)
                .orElseGet(() -> tagsRepository.save(new Tag(tagname)))).collect(Collectors.toSet());

        note.setTags(tags);
        note.setUser(user);
        return notesRepository.save(note);
    }

    public Note updateNote(UUID id, Note newNote, String userEmail) {
        User user = getUser(userEmail);
        Note oldNote = notesRepository.findById(id).orElseThrow();

        if(!oldNote.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized: you do not own this note");
        }

        if (newNote.getTitle() != null && !newNote.getTitle().isBlank())
            oldNote.setTitle(newNote.getTitle());
        if (newNote.getContent() != null && !newNote.getContent().isBlank())
            oldNote.setContent(newNote.getContent());
        if (newNote.getFormat() != null)
            oldNote.setFormat(newNote.getFormat());

        String text = Stream.of(oldNote.getContent(), oldNote.getTitle())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        Set<String> extractedTags = taggingService.extractTagsFromContent(text);

        Set<Tag> tags = extractedTags.stream().map((tagString) -> tagsRepository.findByNameIgnoreCase(tagString)
                .orElseGet(() -> tagsRepository.save(new Tag(tagString)))).collect(Collectors.toSet());

        oldNote.setTags(tags);
        oldNote.setLanguage(newNote.getLanguage());

        return notesRepository.save(oldNote);
    }

    public List<Note> searchNotes(String keyword, String tagName, Format format, String userEmail) {
        User user = getUser(userEmail);
        Specification<Note> spec = Specification.where(NotesSpecification.belongsToUser(user));
        if(keyword != null && !keyword.isBlank()){
            spec = spec.and(NotesSpecification.containsKeyword(keyword));
        }

        if(tagName != null && !tagName.isBlank()){
            spec = spec.and(NotesSpecification.hasTag(tagName));
        }

        if(format != null){
            spec = spec.and(NotesSpecification.hasFormat(format));
        }

        return notesRepository.findAll(spec);
    }

    public void deleteNote(UUID id, String userEmail) {
        User user = getUser(userEmail);
        Note note = notesRepository.findById(id).orElseThrow();
        if(!note.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized");
        }
        notesRepository.deleteById(id);
    }
}
