package com.example.dev_diaries.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev_diaries.dto.NoteResponse;
import com.example.dev_diaries.models.Format;
import com.example.dev_diaries.models.Note;
import com.example.dev_diaries.services.ExportService;
import com.example.dev_diaries.services.NotesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/notes")
public class NotesController {
    NotesService notesService;
    ExportService exportService;

    private NoteResponse convertToDTO(Note note){
        NoteResponse noteResponse = new NoteResponse();
        noteResponse.setContent(note.getContent());
        noteResponse.setCreatedAt(note.getCreatedAt());
        noteResponse.setFormat(note.getFormat());
        noteResponse.setId(note.getId());
        noteResponse.setTitle(note.getTitle());

        if(note.getTags() != null){
            Set<String> tags = note.getTags().stream().map(t -> t.getName()).collect(Collectors.toSet());
            noteResponse.setTags(tags);
        }

        return noteResponse;
    }

    public NotesController(NotesService notesService, ExportService exportService) {
        this.notesService = notesService;
        this.exportService = exportService;
    }

    @GetMapping("/")
    public List<Note> getAllNotes(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String tagName,
        @RequestParam(required = false) Format format,
        Principal principal
    ) {
        return notesService.searchNotes(keyword, tagName, format, principal.getName());
    }

    @PostMapping("/")
    public Note createNote(@RequestBody Note note, Principal principal){
        return notesService.createNote(note, principal.getName());
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable UUID id, @RequestBody Note note, Principal principal){
        return notesService.updateNote(id, note, principal.getName());
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportNotes(
        @RequestParam(required = false) String keyword, 
        @RequestParam(required = false) String tagName,
        @RequestParam(required = false) Format format,
        Principal principal
    ){
        try{
            byte[] zipData = exportService.exportToZip(keyword, tagName, format, principal.getName());
            // System.out.println(zipData.toString());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"dev-diaries-backup.zip\"");

            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(zipData);
            
        }catch(IOException e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable UUID id, Principal principal){
        notesService.deleteNote(id, principal.getName());
    }
}
