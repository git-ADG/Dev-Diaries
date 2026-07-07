package com.example.dev_diaries.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.example.dev_diaries.models.Format;
import com.example.dev_diaries.models.Note;
import com.example.dev_diaries.models.Tag;
import com.example.dev_diaries.models.User;
import com.example.dev_diaries.repositories.NotesRepository;
import com.example.dev_diaries.repositories.TagsRepository;
import com.example.dev_diaries.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class NotesServiceTest {

    @Mock
    private TaggingService taggingService;

    @Mock
    private NotesRepository notesRepository;

    @Mock
    private TagsRepository tagsRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotesService notesService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("owner@example.com");
        user.setPassword("password");
    }

    @Test
    void createNoteAddsUserAndTagsToNote() {
        Note note = new Note();
        note.setTitle("Spring Boot");
        note.setContent("I am writing java code.");

        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(user));
        when(taggingService.extractTagsFromContent("I am writing java code. Spring Boot"))
                .thenReturn(Set.of("Java / Spring"));
        when(tagsRepository.findByNameIgnoreCase("Java / Spring")).thenReturn(Optional.empty());
        when(tagsRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(notesRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Note result = notesService.createNote(note, "owner@example.com");

        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getTags()).extracting(Tag::getName).containsExactly("Java / Spring");
        assertThat(result.getTitle()).isEqualTo("Spring Boot");
        verify(notesRepository).save(any(Note.class));
    }

    @Test
    void updateNoteUpdatesFieldsAndPreservesUser() {
        UUID noteId = UUID.randomUUID();
        Note existingNote = new Note();
        existingNote.setId(noteId);
        existingNote.setTitle("Old Title");
        existingNote.setContent("Old content");
        existingNote.setFormat(Format.PLAIN_TEXT);
        existingNote.setUser(user);

        Note newNote = new Note();
        newNote.setTitle("New Title");
        newNote.setContent("New content for java");
        newNote.setFormat(Format.MARKDOWN);
        newNote.setLanguage("Java");

        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(user));
        when(notesRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
        when(taggingService.extractTagsFromContent("New content for java New Title"))
                .thenReturn(Set.of("Java / Spring"));
        when(tagsRepository.findByNameIgnoreCase("Java / Spring")).thenReturn(Optional.of(new Tag("Java / Spring")));
        when(notesRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Note result = notesService.updateNote(noteId, newNote, "owner@example.com");

        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getContent()).isEqualTo("New content for java");
        assertThat(result.getFormat()).isEqualTo(Format.MARKDOWN);
        assertThat(result.getLanguage()).isEqualTo("Java");
        assertThat(result.getTags()).extracting(Tag::getName).containsExactly("Java / Spring");
    }

    @Test
    void updateNoteThrowsWhenUserDoesNotOwnNote() {
        UUID noteId = UUID.randomUUID();
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());

        Note existingNote = new Note();
        existingNote.setId(noteId);
        existingNote.setTitle("Old Title");
        existingNote.setContent("Old content");
        existingNote.setUser(otherUser);

        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(user));
        when(notesRepository.findById(noteId)).thenReturn(Optional.of(existingNote));

        assertThatThrownBy(() -> notesService.updateNote(noteId, new Note(), "owner@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unauthorized");
    }

    @Test
    void deleteNoteRemovesNoteWhenAuthorized() {
        UUID noteId = UUID.randomUUID();
        Note note = new Note();
        note.setId(noteId);
        note.setUser(user);

        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(user));
        when(notesRepository.findById(noteId)).thenReturn(Optional.of(note));

        notesService.deleteNote(noteId, "owner@example.com");

        verify(notesRepository).deleteById(noteId);
    }

    @Test
    void deleteNoteThrowsWhenUnauthorized() {
        UUID noteId = UUID.randomUUID();
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());

        Note note = new Note();
        note.setId(noteId);
        note.setUser(otherUser);

        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(user));
        when(notesRepository.findById(noteId)).thenReturn(Optional.of(note));

        assertThatThrownBy(() -> notesService.deleteNote(noteId, "owner@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unauthorized");
    }

    @Test
    void searchNotesDelegatesToRepositoryWithSpecification() {
        Note note = new Note();
        note.setTitle("Searchable");
        note.setUser(user);

        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(user));
        when(notesRepository.findAll(any(Specification.class))).thenReturn(List.of(note));

        List<Note> result = notesService.searchNotes("search", "java", Format.CODE, "owner@example.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Searchable");
    }
}
