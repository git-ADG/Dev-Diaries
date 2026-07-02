package com.example.dev_diaries.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

public class APIError {

    private LocalDateTime timeStamp = LocalDateTime.now();
    private int status;
    private String message;
    private Map<String, String> validationErrors;

    public APIError(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }
    
    
}
