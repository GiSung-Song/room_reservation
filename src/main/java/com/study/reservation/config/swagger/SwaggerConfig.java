package com.study.reservation.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("AuthorizationKey");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        Info info = new Info()
                .title("ROOM RESERVATION SYSTEM API Document")
                .version("1.0.0")
                .description("ROOM RESERVATION SYSTEM API 명세서입니다.");

        return new OpenAPI()
                .components(new Components())
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement))
                .info(info);
    }

}
