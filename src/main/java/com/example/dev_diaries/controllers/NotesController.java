package com.example.dev_diaries.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev_diaries.models.Format;
import com.example.dev_diaries.models.Note;
import com.example.dev_diaries.services.ExportService;
import com.example.dev_diaries.services.NotesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/notes")
public class NotesController {
    NotesService notesService;
    ExportService exportService;

    public NotesController(NotesService notesService, ExportService exportService) {
        this.notesService = notesService;
        this.exportService = exportService;
    }

    @GetMapping("/")
    public List<Note> getAllNotes(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String tagName,
        @RequestParam(required = false) Format format
    ) {
        return notesService.searchNotes(keyword, tagName, format);
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

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportNotes(){
        try{
            byte[] zipData = exportService.exportToZip();
            // System.out.println(zipData.toString());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"dev-diaries-backup.zip\"");

            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(zipData);
            
        }catch(IOException e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
