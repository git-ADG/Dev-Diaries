package com.example.dev_diaries.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

import com.example.dev_diaries.models.Format;
import com.example.dev_diaries.models.Note;
import com.example.dev_diaries.models.Tag;
import com.example.dev_diaries.repositories.NotesRepository;

@Service
public class ExportService {
    private final NotesService notesService;

    public ExportService(NotesService notesService) {
        this.notesService = notesService;
    }

    public byte[] exportToZip(String keyword, String tagName, Format format) throws IOException {
        List<Note> notes = notesService.searchNotes(keyword, tagName, format);
        // notes.forEach((note) -> {
        //     System.out.println(note.getId().toString());
        // });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Note note : notes) {
                String title = note.getTitle() != null ? note.getTitle() : "untitled_" + note.getId();

                String safeTitle = title.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

                // System.out.println(safeTitle);

                String uniqueSuffix = note.getId().toString().substring(0, 8);

                safeTitle += uniqueSuffix;

                ZipEntry entry = new ZipEntry(safeTitle + ".md");
                zos.putNextEntry(entry);
                String tagsString = (note.getTags() != null && !note.getTags().isEmpty())
                        ? note.getTags().stream().map(Tag::getName).collect(Collectors.joining(", "))
                        : "No Tags";

                String safeContent = (note.getContent() != null) ? note.getContent() : "";

                String markdownContent = String.format(
                        "# %s\n\n**Tags:** %s\n**Format:** %s\n**Created:** %s\n\n---\n\n%s",
                        title,
                        tagsString,
                        note.getFormat() != null ? note.getFormat().toString() : "UNKNOWN",
                        note.getCreatedAt() != null ? note.getCreatedAt().toString() : "Unknown Date",
                        safeContent);

                zos.write(markdownContent.getBytes(StandardCharsets.UTF_8));
                zos.closeEntry();
            }
        }
        return baos.toByteArray();
    }
}
