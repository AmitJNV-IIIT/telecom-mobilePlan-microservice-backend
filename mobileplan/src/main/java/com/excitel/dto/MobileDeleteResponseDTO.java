package com.excitel.dto;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobileDeleteResponseDTO {
    private HttpStatus status;
    private String message;
}