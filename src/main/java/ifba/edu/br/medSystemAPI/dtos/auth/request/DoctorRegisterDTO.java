package ifba.edu.br.medSystemAPI.dtos.auth.request;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.shared.validators.ValidCRM;
import ifba.edu.br.medSystemAPI.shared.validators.ValidPhone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para registro de médico")
public record DoctorRegisterDTO(
        @NotBlank(message = "O email é obrigatório") @Email(message = "Email inválido") @Schema(description = "Email do médico (deve ser único)", example = "joao.silva@hospital.com") String email,

        @NotBlank(message = "A senha é obrigatória") @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") @Schema(description = "Senha (mínimo 6 caracteres)", example = "senha123") String password,

        @NotBlank(message = "O nome é obrigatório") @Schema(description = "Nome completo do médico", example = "Dr. João Silva") String name,

        @NotBlank(message = "O telefone é obrigatório") @ValidPhone @Schema(description = "Telefone do médico. Formatos aceitos: (00) 00000-0000, (00) 0000-0000, 00000000000", example = "(71) 99999-8888") String phone,

        @NotBlank(message = "O CRM é obrigatório") @ValidCRM @Schema(description = "CRM do médico no formato CRM/UF 000000", example = "CRM/SP 123456") String crm,

        @NotBlank(message = "A especialidade é obrigatória") @Schema(description = "Especialidade médica", example = "CARDIOLOGY") String specialty,

        @Valid @Schema(description = "Endereço completo do médico") AddressRequestDTO address) {
}
