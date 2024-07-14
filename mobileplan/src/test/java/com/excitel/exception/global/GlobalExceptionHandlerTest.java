package com.excitel.exception.global;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.excitel.dto.ErrorResponseDTO;
import com.excitel.exception.custom.CouponsException;
import com.excitel.exception.custom.DatabaseConnectionException;
import com.excitel.exception.custom.DuplicatePhoneNumberException;
import com.excitel.exception.custom.PlanNotFoundException;
import com.excitel.exception.custom.ResourceNotFoundException;
import com.excitel.exception.custom.UserAccessDeniedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {GlobalExceptionHandler.class})
@ExtendWith(SpringExtension.class)
class GlobalExceptionHandlerTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    /**
     * Method under test: {@link GlobalExceptionHandler#handleException(Exception)}
     */
    @Test
    void testHandleException() {
        // Arrange and Act
        ResponseEntity<ErrorResponseDTO> actualHandleExceptionResult = globalExceptionHandler
                .handleException(new Exception("foo"));

        // Assert
        HttpStatusCode expectedStatus = actualHandleExceptionResult.getStatusCode();
        assertSame(expectedStatus, actualHandleExceptionResult.getBody().getStatus());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handleResourceNotFoundException(ResourceNotFoundException)}
     */
    @Test
    void testHandleResourceNotFoundException() {
        // Arrange and Act
        ResponseEntity<ErrorResponseDTO> actualHandleResourceNotFoundExceptionResult = globalExceptionHandler
                .handleResourceNotFoundException(new ResourceNotFoundException("An error occurred"));

        // Assert
        HttpStatusCode expectedStatus = actualHandleResourceNotFoundExceptionResult.getStatusCode();
        assertSame(expectedStatus, actualHandleResourceNotFoundExceptionResult.getBody().getStatus());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handleResourceNotFoundException(ResourceNotFoundException)}
     */
    @Test
    void testHandleResourceNotFoundException2() {
        // Arrange
        ResourceNotFoundException ex = mock(ResourceNotFoundException.class);
        when(ex.getMessage()).thenReturn("Not all who wander are lost");

        // Act
        ResponseEntity<ErrorResponseDTO> actualHandleResourceNotFoundExceptionResult = globalExceptionHandler
                .handleResourceNotFoundException(ex);

        // Assert
        verify(ex).getMessage();
        HttpStatusCode expectedStatus = actualHandleResourceNotFoundExceptionResult.getStatusCode();
        assertSame(expectedStatus, actualHandleResourceNotFoundExceptionResult.getBody().getStatus());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handleUserAccessDeniedException(UserAccessDeniedException)}
     */
    @Test
    void testHandleUserAccessDeniedException() {
        // Arrange and Act
        ResponseEntity<ErrorResponseDTO> actualHandleUserAccessDeniedExceptionResult = globalExceptionHandler
                .handleUserAccessDeniedException(new UserAccessDeniedException("An error occurred"));

        // Assert
        HttpStatusCode expectedStatus = actualHandleUserAccessDeniedExceptionResult.getStatusCode();
        assertSame(expectedStatus, actualHandleUserAccessDeniedExceptionResult.getBody().getStatus());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handleUserAccessDeniedException(UserAccessDeniedException)}
     */
    @Test
    void testHandleUserAccessDeniedException2() {
        // Arrange
        UserAccessDeniedException ex = mock(UserAccessDeniedException.class);
        when(ex.getMessage()).thenReturn("Not all who wander are lost");

        // Act
        ResponseEntity<ErrorResponseDTO> actualHandleUserAccessDeniedExceptionResult = globalExceptionHandler
                .handleUserAccessDeniedException(ex);

        // Assert
        verify(ex).getMessage();
        HttpStatusCode expectedStatus = actualHandleUserAccessDeniedExceptionResult.getStatusCode();
        assertSame(expectedStatus, actualHandleUserAccessDeniedExceptionResult.getBody().getStatus());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handleDatabaseConnectionException(DatabaseConnectionException)}
     */
    @Test
    void testHandleDatabaseConnectionException() {
        // Arrange and Act
        ResponseEntity<ErrorResponseDTO> actualHandleDatabaseConnectionExceptionResult = globalExceptionHandler
                .handleDatabaseConnectionException(new DatabaseConnectionException("An error occurred"));

        // Assert
        HttpStatusCode expectedStatus = actualHandleDatabaseConnectionExceptionResult.getStatusCode();
        assertSame(expectedStatus, actualHandleDatabaseConnectionExceptionResult.getBody().getStatus());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handleDatabaseConnectionException(DatabaseConnectionException)}
     */
    @Test
    void testHandleDatabaseConnectionException2() {
        // Arrange
        DatabaseConnectionException ex = mock(DatabaseConnectionException.class);
        when(ex.getMessage()).thenReturn("Not all who wander are lost");

        // Act
        ResponseEntity<ErrorResponseDTO> actualHandleDatabaseConnectionExceptionResult = globalExceptionHandler
                .handleDatabaseConnectionException(ex);

        // Assert
        verify(ex).getMessage();
        HttpStatusCode expectedStatus = actualHandleDatabaseConnectionExceptionResult.getStatusCode();
        assertSame(expectedStatus, actualHandleDatabaseConnectionExceptionResult.getBody().getStatus());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handlePlanNotFoundException(CouponsException)}
     */
    @Test
    void testHandlePlanNotFoundException() {
        // Arrange and Act
        ResponseEntity<ErrorResponseDTO> actualHandlePlanNotFoundExceptionResult = globalExceptionHandler
                .handlePlanNotFoundException(new CouponsException("An error occurred"));

        // Assert
        HttpStatusCode expectedStatus = actualHandlePlanNotFoundExceptionResult.getStatusCode();
        assertSame(expectedStatus, actualHandlePlanNotFoundExceptionResult.getBody().getStatus());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handlePlanNotFoundException(CouponsException)}
     */
    @Test
    void testHandlePlanNotFoundException2() {
        // Arrange
        CouponsException ex = mock(CouponsException.class);
        when(ex.getMessage()).thenReturn("Not all who wander are lost");

        // Act
        ResponseEntity<ErrorResponseDTO> actualHandlePlanNotFoundExceptionResult = globalExceptionHandler
                .handlePlanNotFoundException(ex);

        // Assert
        verify(ex).getMessage();
        HttpStatusCode expectedStatus = actualHandlePlanNotFoundExceptionResult.getStatusCode();
        assertSame(expectedStatus, actualHandlePlanNotFoundExceptionResult.getBody().getStatus());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handlePlanNotFoundException(PlanNotFoundException)}
     */
    @Test
    void testHandlePlanNotFoundException3() {
        // Arrange and Act
        ResponseEntity<ErrorResponseDTO> actualHandlePlanNotFoundExceptionResult = globalExceptionHandler
                .handlePlanNotFoundException(new PlanNotFoundException("An error occurred"));

        // Assert
        HttpStatusCode expectedStatus = actualHandlePlanNotFoundExceptionResult.getStatusCode();
        assertSame(expectedStatus, actualHandlePlanNotFoundExceptionResult.getBody().getStatus());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handlePlanNotFoundException(PlanNotFoundException)}
     */
    @Test
    void testHandlePlanNotFoundException4() {
        // Arrange
        PlanNotFoundException ex = mock(PlanNotFoundException.class);
        when(ex.getMessage()).thenReturn("Not all who wander are lost");

        // Act
        ResponseEntity<ErrorResponseDTO> actualHandlePlanNotFoundExceptionResult = globalExceptionHandler
                .handlePlanNotFoundException(ex);

        // Assert
        verify(ex).getMessage();
        HttpStatusCode expectedStatus = actualHandlePlanNotFoundExceptionResult.getStatusCode();
        assertSame(expectedStatus, actualHandlePlanNotFoundExceptionResult.getBody().getStatus());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handleDuplicatePhoneNumberException(DuplicatePhoneNumberException)}
     */
    @Test
    void testHandleDuplicatePhoneNumberException() {
        // Arrange and Act
        ResponseEntity<ErrorResponseDTO> actualHandleDuplicatePhoneNumberExceptionResult = globalExceptionHandler
                .handleDuplicatePhoneNumberException(new DuplicatePhoneNumberException("An error occurred"));

        // Assert
        ErrorResponseDTO body = actualHandleDuplicatePhoneNumberExceptionResult.getBody();
        assertEquals("Duplicate values not allowed - An error occurred", body.getErrorMessage());
        assertEquals(404, actualHandleDuplicatePhoneNumberExceptionResult.getStatusCodeValue());
        assertEquals(HttpStatus.CONFLICT, body.getStatus());
        assertTrue(actualHandleDuplicatePhoneNumberExceptionResult.hasBody());
        assertTrue(actualHandleDuplicatePhoneNumberExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link GlobalExceptionHandler#handleDuplicatePhoneNumberException(DuplicatePhoneNumberException)}
     */
    @Test
    void testHandleDuplicatePhoneNumberException2() {
        // Arrange
        DuplicatePhoneNumberException ex = mock(DuplicatePhoneNumberException.class);
        when(ex.getMessage()).thenReturn("Not all who wander are lost");

        // Act
        ResponseEntity<ErrorResponseDTO> actualHandleDuplicatePhoneNumberExceptionResult = globalExceptionHandler
                .handleDuplicatePhoneNumberException(ex);

        // Assert
        verify(ex).getMessage();
        ErrorResponseDTO body = actualHandleDuplicatePhoneNumberExceptionResult.getBody();
        assertEquals("Duplicate values not allowed - Not all who wander are lost", body.getErrorMessage());
        assertEquals(404, actualHandleDuplicatePhoneNumberExceptionResult.getStatusCodeValue());
        assertEquals(HttpStatus.CONFLICT, body.getStatus());
        assertTrue(actualHandleDuplicatePhoneNumberExceptionResult.hasBody());
        assertTrue(actualHandleDuplicatePhoneNumberExceptionResult.getHeaders().isEmpty());
    }
}
