package com.example.dev_diaries.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.junit.jupiter.api.Test;

public class ExportServiceTest {

    @Test
    void exportToZipReturnsEmptyZipWhenThereAreNoNotes() throws IOException {
        NotesService notesService = mock(NotesService.class);
        when(notesService.searchNotes(anyString(), anyString(), any(), anyString())).thenReturn(List.of());

        ExportService exportService = new ExportService(notesService);

        byte[] zipData = exportService.exportToZip("", "", null, "test@example.com");

        assertThat(zipData).isNotEmpty();

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipData))) {
            assertThat(zis.getNextEntry()).isNull();
        }
    }
}
