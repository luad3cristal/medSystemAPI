package ifba.edu.br.medSystemAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // Permite origens específicas (em produção, substitua por domínios reais)
    configuration.setAllowedOriginPatterns(Arrays.asList(
        "http://localhost:*",
        "http://127.0.0.1:*",
        "https://*.vercel.app",
        "https://*.netlify.app"));

    // Métodos HTTP permitidos
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

    // Headers permitidos
    configuration.setAllowedHeaders(List.of("*"));

    // Permite envio de credenciais (cookies, auth headers)
    configuration.setAllowCredentials(true);

    // Headers expostos ao frontend
    configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

    // Tempo de cache da configuração CORS (em segundos)
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
