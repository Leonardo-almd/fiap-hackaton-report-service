package br.com.fiap.report.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Report Service API")
                .description("Relatórios de análise arquitetural gerados pelo pipeline de IA.")
                .version("1.0.0"));
    }
}
