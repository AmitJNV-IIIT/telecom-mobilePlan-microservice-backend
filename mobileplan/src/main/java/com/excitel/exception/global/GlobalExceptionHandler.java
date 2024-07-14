package com.excitel.exception.global;

import com.excitel.dto.ErrorResponseDTO;
import com.excitel.exception.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Global exception handler for handling exceptions thrown from controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String DATABASE_CONNECTION_ERROR_MESSAGE = "Error occurred while connecting to DB: ";
    /**
     * Handles generic exceptions and returns an internal server error response.
     *
     * @param ex The exception to handle.
     * @return   ResponseEntity containing an error response.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex) {
        ErrorResponseDTO responseDTO = ErrorResponseDTO.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).errorMessage("An internal server error occurred: " + ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }
    /**
     * Handles ResourceNotFoundException and returns an internal server error response.
     *
     * @param ex The exception to handle.
     * @return   ResponseEntity containing an error response.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponseDTO responseDTO = ErrorResponseDTO.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).errorMessage("Resource not found: " + ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

    /**
     * Handles UserAccessDeniedException and returns an unauthorized response.
     *
     * @param ex The exception to handle.
     * @return   ResponseEntity containing an error response.
     */
    @ExceptionHandler(UserAccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAccessDeniedException(UserAccessDeniedException ex) {
        ErrorResponseDTO responseDTO = ErrorResponseDTO.builder().status(HttpStatus.UNAUTHORIZED).errorMessage("This route is only for admins " + ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
    }

    /**
     * Handles DatabaseConnectionException and returns an internal server error response.
     *
     * @param ex The exception to handle.
     * @return   ResponseEntity containing an error response.
     */

    @ExceptionHandler(DatabaseConnectionException.class)
    public ResponseEntity<ErrorResponseDTO> handleDatabaseConnectionException(DatabaseConnectionException ex) {
        ErrorResponseDTO responseDTO = ErrorResponseDTO.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).errorMessage(DATABASE_CONNECTION_ERROR_MESSAGE + ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }
    /**
     * Handles PlanNotFoundException and returns a not found response.
     *
     * @param ex The exception to handle.
     * @return   ResponseEntity containing an error response.
     */

    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlePlanNotFoundException(PlanNotFoundException ex) {
        ErrorResponseDTO responseDTO = ErrorResponseDTO.builder().status(HttpStatus.NOT_FOUND).errorMessage(DATABASE_CONNECTION_ERROR_MESSAGE + ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    /**
     * Handles CouponsException and returns a not found response.
     *
     * @param ex The exception to handle.
     * @return   ResponseEntity containing an error response.
     */
    @ExceptionHandler(CouponsException.class)
    public ResponseEntity<ErrorResponseDTO> handlePlanNotFoundException(CouponsException ex) {
        ErrorResponseDTO responseDTO = ErrorResponseDTO.builder().status(HttpStatus.NOT_FOUND).errorMessage(DATABASE_CONNECTION_ERROR_MESSAGE + ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }


    @ExceptionHandler(DuplicatePhoneNumberException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicatePhoneNumberException(DuplicatePhoneNumberException ex) {
        ErrorResponseDTO responseDTO = ErrorResponseDTO.builder().status(HttpStatus.CONFLICT).errorMessage("Duplicate values not allowed - " + ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }



}
