package com.example.Bookstore;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Мой первый Spring проект!",
                description = "Мои первые (и, надеюсь, не последние) попытки освоения Spring",
                version = "1.0.0",
                contact = @Contact(
                        name = "Wren",
                        email = "i.dont.know.what.to.write122@gmail.com"
                )
        )
)

public class OpenApiConfig {
}
