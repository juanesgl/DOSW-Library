package edu.eci.dosw.tdd.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de la Biblioteca DOSW")
                        .version("1.0.0")
                        .description("Documentación interactiva con Swagger UI para la gestión de libros, usuarios y préstamos de la empresa DOSW."));
    }
}