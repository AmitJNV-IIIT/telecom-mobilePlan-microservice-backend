package com.excitel.dto;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ResponseDTO {
    private HttpStatus status;
    private String message;
    private String mobileNumber;
    private String email;
    private String role;

    public ResponseDTO(HttpStatus status, String errorMessage, String phoneNumber, String email, String role) {
        this.status = status;
        this.message = errorMessage;
        this.mobileNumber = phoneNumber;
        this.email = email;
        this.role = role;


    }

    public ResponseDTO(HttpStatus status, String errorMessage) {
        this.status = status;
        this.message = errorMessage;
    }
}