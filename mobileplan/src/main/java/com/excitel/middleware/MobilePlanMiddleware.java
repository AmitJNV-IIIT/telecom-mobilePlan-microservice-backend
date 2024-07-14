package com.excitel.middleware;

import com.excitel.dto.ResponseDTO;
import com.excitel.external.TokenValidation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/**
 * Middleware for handling authentication and authorization of requests.
 */

@Component
public class MobilePlanMiddleware extends OncePerRequestFilter {

    @Autowired//NOSONAR
    private TokenValidation tokenValidationService;

    private static final String API_V1_PREFIX = "/api/v2/";
    private static final String COUPON_DATA_API = "/api/v2/mobile/coupon/data/";
    /**
     * Determines whether the filter should be applied to the given request.
     *
     * @param request The HTTP servlet request.
     * @return        True if the filter should not be applied, false otherwise.
     * @throws ServletException If an exception occurs during filtering.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return (!path.startsWith(API_V1_PREFIX)
                || (path.equals("/api/v2/mobile") && request.getMethod().equals(HttpMethod.GET.name()))
                || path.equals("/api/v2/mobile/health")
                || path.equals("/api/v2/mobile/coupon-details")
                ||path.equals("/api/v2/mobile/actuator")
                || path.equals("/api/v2/mobile/actuator/prometheus")
                || (path.startsWith("/api/v2/mobile/coupon-limit/"))
                || path.startsWith(COUPON_DATA_API)
        );
    }
    /**
     * Performs the actual filtering logic for each incoming request.
     *
     * @param request     The HTTP servlet request.
     * @param response    The HTTP servlet response.
     * @param filterChain The filter chain.
     * @throws ServletException If an exception occurs during filtering.
     * @throws IOException      If an I/O exception occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authToken = request.getHeader("Authorization");
        if (authToken == null || authToken.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header missing");
            return;
        }

        ResponseEntity<ResponseDTO> responseEntity = tokenValidationService.isValid(authToken);

        if (responseEntity.getBody().getStatus() == HttpStatus.OK) {//NOSONAR
            request.setAttribute("email", responseEntity.getBody().getEmail());//NOSONAR
            request.setAttribute("mobileNumber", responseEntity.getBody().getMobileNumber());//NOSONAR
            request.setAttribute("role", responseEntity.getBody().getRole());//NOSONAR
            filterChain.doFilter(request, response);
        } else {

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Invalid authorization token");
        }
    }
}