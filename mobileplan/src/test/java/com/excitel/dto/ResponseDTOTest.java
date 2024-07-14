package com.excitel.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ResponseDTOTest {
    /**
     * Method under test: {@link ResponseDTO#ResponseDTO(HttpStatus, String)}
     */
    @Test
    void testNewResponseDTO() {
        // Arrange and Act
        ResponseDTO actualResponseDTO = new ResponseDTO(HttpStatus.CONTINUE, "An error occurred");

        // Assert
        assertEquals("An error occurred", actualResponseDTO.getMessage());
        assertNull(actualResponseDTO.getEmail());
        assertNull(actualResponseDTO.getMobileNumber());
        assertNull(actualResponseDTO.getRole());
        assertEquals(HttpStatus.CONTINUE, actualResponseDTO.getStatus());
    }

    /**
     * Method under test:
     * {@link ResponseDTO#ResponseDTO(HttpStatus, String, String, String, String)}
     */
    @Test
    void testNewResponseDTO2() {
        // Arrange and Act
        ResponseDTO actualResponseDTO = new ResponseDTO(HttpStatus.CONTINUE, "An error occurred", "6625550144",
                "jane.doe@example.org", "Role");

        // Assert
        assertEquals("6625550144", actualResponseDTO.getMobileNumber());
        assertEquals("An error occurred", actualResponseDTO.getMessage());
        assertEquals("Role", actualResponseDTO.getRole());
        assertEquals("jane.doe@example.org", actualResponseDTO.getEmail());
        assertEquals(HttpStatus.CONTINUE, actualResponseDTO.getStatus());
    }
}
