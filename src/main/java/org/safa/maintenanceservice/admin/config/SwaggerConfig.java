package org.safa.maintenanceservice.admin.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class SwaggerConfig {

    /*
     * This static block runs once when the class is defined*/
    static {
        // Formats the exact time the application server started up (e.g., "14:00:00")
        String currentHour = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:00:00"));

        // Forces Swagger to treat ALL LocalTime fields as strings pre-filled with the current hour
        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class,
                new StringSchema()
                        .example(currentHour)
                        .pattern("HH:mm:ss")
                        .description("Defaulted to server startup hour")
        );
    }
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "BearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT"))
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
    }
}
