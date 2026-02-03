package ifba.edu.br.medSystemAPI.dtos.auth.response;

public record LoginResponseDTO(
  String token,
  String role,
  String name,
  Long userId
) {}
