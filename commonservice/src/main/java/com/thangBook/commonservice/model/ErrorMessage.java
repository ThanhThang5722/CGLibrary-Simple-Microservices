package com.thangBook.commonservice.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorMessage {
    private String code;
    private String message;
    private HttpStatus status;
    public ErrorMessage(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
    public ErrorMessage() {
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
