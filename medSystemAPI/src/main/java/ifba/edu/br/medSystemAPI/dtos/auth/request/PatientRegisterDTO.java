package ifba.edu.br.medSystemAPI.dtos.auth.request;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.shared.validators.ValidPhone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

@Schema(description = "Dados para registro de paciente")
public record PatientRegisterDTO(
        @NotBlank(message = "O email é obrigatório") @Email(message = "Email inválido") @Schema(description = "Email do paciente (deve ser único)", example = "maria.santos@email.com") String email,

        @NotBlank(message = "A senha é obrigatória") @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") @Schema(description = "Senha (mínimo 6 caracteres)", example = "senha123") String password,

        @NotBlank(message = "O nome é obrigatório") @Schema(description = "Nome completo do paciente", example = "Maria Santos") String name,

        @NotBlank(message = "O telefone é obrigatório") @ValidPhone @Schema(description = "Telefone do paciente. Formatos aceitos: (00) 00000-0000, (00) 0000-0000, 00000000000", example = "(71) 98765-4321") String phone,

        @NotBlank(message = "O CPF é obrigatório") @CPF(message = "CPF inválido") @Schema(description = "CPF do paciente (com ou sem formatação). Formato: 000.000.000-00 ou 00000000000", example = "123.456.789-00") String cpf,

        @Valid @Schema(description = "Endereço completo do paciente") AddressRequestDTO address) {
}
