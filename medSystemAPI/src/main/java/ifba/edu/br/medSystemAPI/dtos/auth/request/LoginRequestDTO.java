package ifba.edu.br.medSystemAPI.dtos.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
  @NotBlank(message = "O email é obrigatório")
  @Email(message = "Email inválido")
  String email,

  @NotBlank(message = "A senha é obrigatória")
  String password
) {}
