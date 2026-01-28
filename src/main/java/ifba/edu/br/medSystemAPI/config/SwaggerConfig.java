package ifba.edu.br.medSystemAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
  
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
    .info(new Info()
      .title("MedSystem API")
      .version("1.0.0")
      .description("API RESTful para Sistema de Gestão Médica - Gerenciamento de médicos, pacientes e consultas")
      .contact(new Contact()
        .name("Equipe MedSystem")
        .url("https://github.com/luad3cristal/medSystemAPI")
      )
    );
  }
}
