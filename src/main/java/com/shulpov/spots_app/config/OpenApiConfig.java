package com.shulpov.spots_app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "REST API карты спотов",
                description = "Карта спотов", version = "1.0.0",
                contact = @Contact(
                        name = "Shulpov Victor",
                        email = "vshulpov@gmail.com"
//                        url = "https://localhost:8089"
                )
        )
)
public class OpenApiConfig {

}
