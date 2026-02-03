package ifba.edu.br.medSystemAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
  
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
    .info(new Info()
      .title("MedSystem API")
      .version("1.0.0")
      .description("API RESTful para Sistema de Gestão Médica - Gerenciamento de médicos, pacientes e consultas com autenticação JWT")
      .contact(new Contact()
        .name("Equipe MedSystem")
        .url("https://github.com/luad3cristal/medSystemAPI")
      )
    )
    .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
    .components(new Components()
      .addSecuritySchemes("bearer-jwt", new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .in(SecurityScheme.In.HEADER)
        .name("Authorization")
        .description("Insira o token JWT obtido no endpoint /auth/login")
      )
    );
  }
}
