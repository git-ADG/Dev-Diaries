package com.example.dev_diaries.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.junit.jupiter.api.Test;

import com.example.dev_diaries.repositories.NotesRepository;

public class ExportServiceTest {

    @Test
    void exportToZipReturnsEmptyZipWhenThereAreNoNotes() throws IOException {
        NotesRepository notesRepository = mock(NotesRepository.class);
        when(notesRepository.findAll()).thenReturn(List.of());

        ExportService exportService = new ExportService(notesRepository);

        byte[] zipData = exportService.exportToZip();

        assertThat(zipData).isNotEmpty();

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipData))) {
            assertThat(zis.getNextEntry()).isNull();
        }
    }
}
