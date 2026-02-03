package ifba.edu.br.medSystemAPI.dtos.auth.response;

import ifba.edu.br.medSystemAPI.models.entities.User;

import java.time.LocalDateTime;

public record PendingUserDTO(
    Long id,
    String email,
    String role,
    String name,
    LocalDateTime createdAt) {
  public PendingUserDTO(User user) {
    this(
        user.getId(),
        user.getEmail(),
        user.getRole().name(),
        user.getName(),
        user.getCreatedAt());
  }
}
