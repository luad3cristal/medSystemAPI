package ifba.edu.br.medSystemAPI.dtos.auth.request;

import jakarta.validation.constraints.NotNull;

public record UserApprovalDTO(
    @NotNull(message = "O ID do usuário é obrigatório") Long userId,

    @NotNull(message = "O status de aprovação é obrigatório") Boolean approved) {
}
