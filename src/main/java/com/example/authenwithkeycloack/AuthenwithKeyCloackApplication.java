package com.example.authenwithkeycloack;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication()
@SecurityScheme(
        name = "Authorization",
        scheme = "Bearer",
        bearerFormat = "Bearer [token]",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
@OpenAPIDefinition(security =  @SecurityRequirement(name = "Authorization"))
public class AuthenwithKeyCloackApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenwithKeyCloackApplication.class, args);
    }

}
