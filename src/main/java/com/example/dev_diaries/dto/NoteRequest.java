package com.example.dev_diaries.dto;

import com.example.dev_diaries.models.Format;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NoteRequest {
    @NotNull(message = "Title cannot be empty")
    @Size(max = 100, message = "title cannot exceed 100 characters")
    private String title;

    @NotNull(message = "content cannot be empty")
    private String content;

    @NotNull(message = "format must be {CODE, PLAIN_TEXT, MARKDOWN}")
    private Format format;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

}
