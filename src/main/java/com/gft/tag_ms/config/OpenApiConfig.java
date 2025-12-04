package com.gft.tag_ms.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Tag-Service API")
                        .version("1.0.0")
                        .description("""
                                Serviço responsável pela gestão de Etiquetas em uma arquitetura de microserviços.
                                - Gerenciar o cadastro de Etiquetas (CRUD);
                                - Validar a existência de uma Etiqueta para o Relationship-Service;
                                - Expor dados básicos para outros microsserviços;
                                - Fornecer consulta de Palavras associadas (via Relationship-Service).
                                """)
                        .contact(new Contact()
                                .name("Daniel Nascimento")
                                .email("Daniel.Tomaz@gft.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName)); // ⚡ obrigando JWT
    }
}