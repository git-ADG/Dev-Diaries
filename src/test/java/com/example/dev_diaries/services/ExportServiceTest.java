package com.example.dev_diaries.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.jupiter.api.Test;

import com.example.dev_diaries.models.Format;
import com.example.dev_diaries.models.Note;
import com.example.dev_diaries.models.Tag;

public class ExportServiceTest {

    @Test
    void exportToZipReturnsEmptyZipWhenThereAreNoNotes() throws IOException {
        NotesService notesService = mock(NotesService.class);
        when(notesService.searchNotes(any(), any(), any(), anyString())).thenReturn(List.of());

        ExportService exportService = new ExportService(notesService);

        byte[] zipData = exportService.exportToZip("", "", null, "test@example.com");

        assertThat(zipData).isNotEmpty();

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipData))) {
            assertThat(zis.getNextEntry()).isNull();
        }
    }

    @Test
    void exportToZipCreatesMarkdownFileForNoteWithTitleAndTags() throws IOException {
        NotesService notesService = mock(NotesService.class);
        Note note = new Note();
        note.setId(UUID.randomUUID());
        note.setTitle("My Note");
        note.setContent("Note content");
        note.setFormat(Format.MARKDOWN);
        note.setTags(Set.of(new Tag("Java / Spring")));

        when(notesService.searchNotes(any(), any(), any(), anyString())).thenReturn(List.of(note));

        ExportService exportService = new ExportService(notesService);
        byte[] zipData = exportService.exportToZip("keyword", "tag", Format.MARKDOWN, "test@example.com");

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipData))) {
            ZipEntry entry = zis.getNextEntry();
            assertThat(entry).isNotNull();
            assertThat(entry.getName()).contains("My_Note");

            byte[] buffer = zis.readAllBytes();
            String content = new String(buffer, StandardCharsets.UTF_8);
            assertThat(content).contains("# My Note");
            assertThat(content).contains("**Tags:** Java / Spring");
            assertThat(content).contains("**Format:** MARKDOWN");
            assertThat(content).contains("Note content");
        }
    }

    @Test
    void exportToZipGeneratesUntitledFileAndNoTagsWhenNoteMissingTitleAndTags() throws IOException {
        NotesService notesService = mock(NotesService.class);
        Note note = new Note();
        note.setId(UUID.randomUUID());
        note.setContent(null);
        note.setFormat(null);

        when(notesService.searchNotes(any(), any(), any(), anyString())).thenReturn(List.of(note));

        ExportService exportService = new ExportService(notesService);
        byte[] zipData = exportService.exportToZip(null, null, null, "test@example.com");

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipData))) {
            ZipEntry entry = zis.getNextEntry();
            assertThat(entry).isNotNull();
            assertThat(entry.getName()).startsWith("untitled_");

            byte[] buffer = zis.readAllBytes();
            String content = new String(buffer, StandardCharsets.UTF_8);
            assertThat(content).contains("# untitled_");
            assertThat(content).contains("**Tags:** No Tags");
            assertThat(content).contains("**Format:** UNKNOWN");
            assertThat(content).contains("**Created:** Unknown Date");
        }
    }
}
