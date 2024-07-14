package com.excitel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for OpenAPI setup.
 */
@Configuration
public class OpenAPIConfig {
    @Value("${mobile.openapi.dev-url}")
    private String devUrl;

    @Value("${mobile.openapi.prod-url}")
    private String prodUrl;
    /**
     * Creates and configures the OpenAPI specification.
     *
     * @return the configured OpenAPI specification
     */
    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in local Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production development environment");

        Contact contact = new Contact();
        contact.setEmail("ExcitelCare@gmail.com");
        contact.setName("Excitel");
        contact.setUrl("https://www.excitel.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Excitel Mobile Microservice Management API's")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to operate Excitel Broadband Subscription Microservice.")
                .termsOfService("Excitel Terms & Condition")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
