package com.excitel.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OpenAPIConfig.class})
@ExtendWith(SpringExtension.class)
class OpenAPIConfigTest {
    @Autowired
    private OpenAPIConfig openAPIConfig;

    /**
     * Method under test: {@link OpenAPIConfig#myOpenAPI()}
     */
    @Test
    void testMyOpenAPI() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange and Act
        OpenAPI actualMyOpenAPIResult = (new OpenAPIConfig()).myOpenAPI();

        // Assert
        Info info = actualMyOpenAPIResult.getInfo();
        assertEquals("1.0", info.getVersion());
        assertEquals("3.0.1", actualMyOpenAPIResult.getOpenapi());
        assertEquals("Excitel Mobile Microservice Management API's", info.getTitle());
        assertEquals("Excitel Terms & Condition", info.getTermsOfService());
        Contact contact = info.getContact();
        assertEquals("Excitel", contact.getName());
        assertEquals("ExcitelCare@gmail.com", contact.getEmail());
        License license = info.getLicense();
        assertEquals("MIT License", license.getName());
        List<Server> servers = actualMyOpenAPIResult.getServers();
        assertEquals(2, servers.size());
        Server getResult = servers.get(1);
        assertEquals("Server URL in Production development environment", getResult.getDescription());
        Server getResult2 = servers.get(0);
        assertEquals("Server URL in local Development environment", getResult2.getDescription());
        assertEquals("This API exposes endpoints to operate Excitel Broadband Subscription Microservice.",
                info.getDescription());
        assertEquals("https://choosealicense.com/licenses/mit/", license.getUrl());
        assertEquals("https://www.excitel.com", contact.getUrl());
        assertNull(actualMyOpenAPIResult.getComponents());
        assertNull(actualMyOpenAPIResult.getExternalDocs());
        assertNull(actualMyOpenAPIResult.getPaths());
        assertNull(getResult2.getVariables());
        assertNull(getResult.getVariables());
        assertNull(info.getSummary());
        assertNull(license.getIdentifier());
        assertNull(getResult2.getUrl());
        assertNull(getResult.getUrl());
        assertNull(actualMyOpenAPIResult.getSecurity());
        assertNull(actualMyOpenAPIResult.getTags());
        assertNull(actualMyOpenAPIResult.getWebhooks());
        assertNull(actualMyOpenAPIResult.getExtensions());
        assertNull(contact.getExtensions());
        assertNull(info.getExtensions());
        assertNull(license.getExtensions());
        assertNull(getResult2.getExtensions());
        assertNull(getResult.getExtensions());
        assertEquals(SpecVersion.V30, actualMyOpenAPIResult.getSpecVersion());
    }

    /**
     * Method under test: {@link OpenAPIConfig#myOpenAPI()}
     */
    @Test
    void testMyOpenAPI2() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Diffblue AI was unable to find a test

        // Arrange and Act
        openAPIConfig.myOpenAPI();
    }
}
