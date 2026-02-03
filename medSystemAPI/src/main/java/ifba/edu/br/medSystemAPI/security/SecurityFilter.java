package ifba.edu.br.medSystemAPI.security;

import ifba.edu.br.medSystemAPI.repositories.UserRepository;
import ifba.edu.br.medSystemAPI.services.JWTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

  private final JWTokenService tokenService;
  private final UserRepository userRepository;

  public SecurityFilter(JWTokenService tokenService, UserRepository userRepository) {
    this.tokenService = tokenService;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    var token = recuperarToken(request);

    if (token != null) {
      var email = tokenService.getSubject(token);
      var usuario = userRepository.findByEmail(email);
      var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

  private String recuperarToken(HttpServletRequest request) {
    var token = request.getHeader("Authorization");
    if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
      return null;
    }
    return token.replace("Bearer ", "");
  }
}
