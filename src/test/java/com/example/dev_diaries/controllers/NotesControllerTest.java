package com.example.dev_diaries.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.dev_diaries.dto.NoteRequest;
import com.example.dev_diaries.models.Format;
import com.example.dev_diaries.models.Note;
import com.example.dev_diaries.models.Tag;
import com.example.dev_diaries.services.ExportService;
import com.example.dev_diaries.services.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class NotesControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private NotesService notesService;

    @Mock
    private ExportService exportService;

    @InjectMocks
    private NotesController notesController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(notesController).build();
    }

    @Test
    void getAllNotesReturnsNoteList() throws Exception {
        Note note = new Note();
        note.setId(UUID.randomUUID());
        note.setTitle("My Note");
        note.setContent("Content");
        note.setFormat(Format.MARKDOWN);
        note.setTags(Set.of(new Tag("Java / Spring")));

        when(notesService.searchNotes(any(), any(), any(), anyString())).thenReturn(List.of(note));

        Principal principal = () -> "user@example.com";

        mockMvc.perform(get("/api/notes/").principal(principal).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("My Note"));
    }

    @Test
    void createNoteReturnsCreatedNote() throws Exception {
        NoteRequest request = new NoteRequest();
        request.setTitle("New Note");
        request.setContent("Body");
        request.setFormat(Format.CODE);
        request.setLanguage("Java");

        Note note = new Note();
        note.setId(UUID.randomUUID());
        note.setTitle("New Note");
        note.setContent("Body");
        note.setFormat(Format.CODE);
        note.setLanguage("Java");

        when(notesService.createNote(any(Note.class), anyString())).thenReturn(note);

        Principal principal = () -> "user@example.com";

        mockMvc.perform(post("/api/notes/")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Note"))
                .andExpect(jsonPath("$.language").value("Java"));
    }

    @Test
    void updateNoteReturnsUpdatedNote() throws Exception {
        NoteRequest request = new NoteRequest();
        request.setTitle("Updated");
        request.setContent("Updated body");
        request.setFormat(Format.MARKDOWN);

        UUID id = UUID.randomUUID();
        Note updatedNote = new Note();
        updatedNote.setId(id);
        updatedNote.setTitle("Updated");
        updatedNote.setContent("Updated body");
        updatedNote.setFormat(Format.MARKDOWN);

        when(notesService.updateNote(any(UUID.class), any(Note.class), anyString())).thenReturn(updatedNote);

        Principal principal = () -> "user@example.com";

        mockMvc.perform(put("/api/notes/" + id)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void deleteNoteReturnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();

        Principal principal = () -> "user@example.com";

        mockMvc.perform(delete("/api/notes/" + id).principal(principal))
                .andExpect(status().isOk());
    }
}
