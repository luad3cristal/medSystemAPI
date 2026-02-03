package ifba.edu.br.medSystemAPI.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import ifba.edu.br.medSystemAPI.models.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JWTokenService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration.hours}")
  private int expirationHours;

  public String gerarToken(User user) {
    try {
      var algoritmo = Algorithm.HMAC256(secret);
      return JWT.create()
          .withIssuer("MedSystemAPI")
          .withSubject(user.getEmail())
          .withClaim("role", user.getRole().name())
          .withClaim("userId", user.getId())
          .withClaim("name", user.getName())
          .withExpiresAt(dataExpiracao())
          .sign(algoritmo);
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Erro ao gerar token JWT", exception);
    }
  }

  public String getSubject(String tokenJWT) {
    try {
      var algoritmo = Algorithm.HMAC256(secret);
      return JWT.require(algoritmo)
          .withIssuer("MedSystemAPI")
          .build()
          .verify(tokenJWT)
          .getSubject();
    } catch (JWTVerificationException exception) {
      throw new RuntimeException("Token JWT inválido ou expirado!", exception);
    }
  }

  private java.time.Instant dataExpiracao() {
    return LocalDateTime.now()
        .plusHours(expirationHours)
        .toInstant(ZoneOffset.of("-03:00"));
  }
}
