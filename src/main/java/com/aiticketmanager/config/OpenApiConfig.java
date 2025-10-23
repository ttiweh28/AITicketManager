package com.aiticketmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("AI Ticket Manager API")
                .version("1.0.0")
                .description("API documentation for AI-driven ticket classification and management system.")
                .contact(new Contact()
                    .name("Hewitt Tusiime")
                    .email("hewitt@gmail.com")
                    .url("https://github.com/hewitt-tusiime"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://springdoc.org")));
    }
}
