package com.shulpov.spots_app.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "REST API карты спотов",
                description = "Карта спотов", version = "1.0.0",
                contact = @Contact(
                        name = "Shulpov Victor",
                        email = "vshulpov@gmail.com"
//                        url = "https://localhost:8089"
                )
        ),
        servers = {
                @Server(
                        description = "Local server ENV",
                        url = "http://localhost:8089"
                ),
                @Server(
                        description = "Test server ENV (not working)",
                        url = "http://localhost:9999" //адреса тестового сервера
                ),
                @Server(
                        description = "Prod server ENV (not working)",
                        url = "http://localhost:9999" //адрес прод сервера
                )
        }
)
        @SecurityScheme(
                name = "accessTokenAuth",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT",
                in = SecuritySchemeIn.HEADER,
                paramName = "Bearer",
                description = "Access-токен для аутентификации запросов"
        )
public class OpenApiConfig {

}
