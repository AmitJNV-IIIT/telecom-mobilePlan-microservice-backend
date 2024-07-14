package com.excitel.middleware;

import com.excitel.dto.ResponseDTO;
import com.excitel.external.TokenValidation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MobilePlanMiddlewareTest {

    @Mock
    private TokenValidation tokenValidationService;

    @InjectMocks
    private MobilePlanMiddleware mobilePlanMiddleware;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    PrintWriter printWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldNotFilter_ValidRequest() throws ServletException, IOException {
        // Given
        when(request.getServletPath()).thenReturn("/api/v2/mobile");
        when(request.getHeader("Authorization")).thenReturn("validToken");

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatus(HttpStatus.OK);
        responseDTO.setEmail("test@example.com");
        responseDTO.setMobileNumber("1234567890");
        responseDTO.setRole("user");
        ResponseEntity<ResponseDTO> responseEntity = ResponseEntity.ok(responseDTO);
        when(tokenValidationService.isValid("validToken")).thenReturn(responseEntity);

        // When
        mobilePlanMiddleware.doFilterInternal(request, response, filterChain);

        // Then
        verify(request).setAttribute("email", "test@example.com");
        verify(request).setAttribute("mobileNumber", "1234567890");
        verify(request).setAttribute("role", "user");
        verify(filterChain).doFilter(request, response);
    }
    @Test
    void shouldNotFilter_PathStartingWithPrefix() throws ServletException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/v2/somepath");

        // When
        boolean shouldNotFilter = mobilePlanMiddleware.shouldNotFilter(request);

        // Then
        assertFalse(shouldNotFilter);
    }

    @Test
    void shouldNotFilter_MobileHealthPath() throws ServletException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/v2/mobile/health");

        // When
        boolean shouldNotFilter = mobilePlanMiddleware.shouldNotFilter(request);

        // Then
        assertTrue(shouldNotFilter);
    }

    @Test
    void shouldNotFilter_MobileCouponDetailsPath() throws ServletException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/v2/mobile/coupon-details");

        // When
        boolean shouldNotFilter = mobilePlanMiddleware.shouldNotFilter(request);

        // Then
        assertTrue(shouldNotFilter);
    }

    @Test
    void shouldNotFilter_MobileActuatorPath() throws ServletException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/v2/mobile/actuator");

        // When
        boolean shouldNotFilter = mobilePlanMiddleware.shouldNotFilter(request);

        // Then
        assertTrue(shouldNotFilter);
    }

    @Test
    void shouldNotFilter_MobileActuatorPrometheusPath() throws ServletException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/v2/mobile/actuator/prometheus");

        // When
        boolean shouldNotFilter = mobilePlanMiddleware.shouldNotFilter(request);

        // Then
        assertTrue(shouldNotFilter);
    }


    @Test
    void shouldNotFilter_OtherPath() throws ServletException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/v2/otherpath");

        // When
        boolean shouldNotFilter = mobilePlanMiddleware.shouldNotFilter(request);

        // Then
        assertFalse(shouldNotFilter);
    }

    @Test
    void shouldNotFilter_PathEqualsMobileAndMethodIsGet() throws ServletException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/v2/mobile");
        when(request.getMethod()).thenReturn(HttpMethod.GET.name());

        // When
        boolean shouldNotFilter = mobilePlanMiddleware.shouldNotFilter(request);

        // Then
        assertTrue(shouldNotFilter);
    }

    @Test
    void shouldNotFilter_PathStartsWithMobileCouponLimit() throws ServletException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/api/v2/mobile/coupon-limit/somepath");

        // When
        boolean shouldNotFilter = mobilePlanMiddleware.shouldNotFilter(request);

        // Then
        assertTrue(shouldNotFilter);
    }

    @Test
    void shouldNotFilter_PathStartsWithCouponDataApi() throws ServletException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/coupon-data-api/somepath");

        // When
        boolean shouldNotFilter = mobilePlanMiddleware.shouldNotFilter(request);

        // Then
        assertTrue(shouldNotFilter);
    }
    @Test
    public void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String authToken = "validToken";
        when(request.getHeader("Authorization")).thenReturn(authToken);
        ResponseDTO responseDTO = new ResponseDTO(); // replace with actual implementation
        responseDTO.setStatus(HttpStatus.OK);
        when(tokenValidationService.isValid(authToken)).thenReturn(ResponseEntity.ok(responseDTO));

        // When
        mobilePlanMiddleware.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
    }

@Test
public void testDoFilterInternal_InvalidAuthorizationToken() throws Exception {
    // Given
    String invalidToken = "invalidToken";
    when(request.getHeader("Authorization")).thenReturn(invalidToken);
    when(tokenValidationService.isValid(invalidToken)).thenReturn(ResponseEntity.ok(new ResponseDTO(HttpStatus.FORBIDDEN, "Invalid token", null, null, null)));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    // When
    mobilePlanMiddleware.doFilterInternal(request, response, filterChain);

    // Then
    assertEquals("Invalid authorization token", stringWriter.toString());
    verify(filterChain, times(0)).doFilter(any(), any());
}


    @Test
    public void testDoFilterInternal_MissingAuthorizationToken() throws Exception {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // When
        mobilePlanMiddleware.doFilterInternal(request, response, filterChain);

        // Then
        assertEquals("Authorization header missing", stringWriter.toString());
        verify(filterChain, times(0)).doFilter(any(), any());
    }


    @Test
    public void whenAuthorizationHeaderMissing_thenUnauthorized() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        // When
        new MobilePlanMiddleware().doFilterInternal(request, response, filterChain);

        // Then
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Authorization header missing", response.getContentAsString());
    }

    @Test
    public void whenAuthorizationHeaderEmpty_thenUnauthorized() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        // When
        new MobilePlanMiddleware().doFilterInternal(request, response, filterChain);

        // Then
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Authorization header missing", response.getContentAsString());
    }
}
