package com.webcrawler.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "WebCrawler API",
                version = "1.0",
                description = "API documentation for WebCrawler"
        )
)
public class SwaggerConfig {
}

