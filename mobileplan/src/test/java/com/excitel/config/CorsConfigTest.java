package com.excitel.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@ContextConfiguration(classes = {CorsConfig.class})
@ExtendWith(SpringExtension.class)
class CorsConfigTest {
    @Autowired
    private CorsConfig corsConfig;

    /**
     * Method under test: {@link CorsConfig#corsConfigurationSource()}
     */
    @Test
    void testCorsConfigurationSource() {
        // Arrange, Act and Assert
        assertTrue(corsConfig.corsConfigurationSource() instanceof UrlBasedCorsConfigurationSource);
    }
}
