package ifba.edu.br.medSystemAPI.dtos.auth.request;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.validators.ValidCRM;
import ifba.edu.br.medSystemAPI.validators.ValidPhone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DoctorRegisterDTO(
        @NotBlank(message = "O email é obrigatório") @Email(message = "Email inválido") String email,

        @NotBlank(message = "A senha é obrigatória") @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String password,

        @NotBlank(message = "O nome é obrigatório") String name,

        @NotBlank(message = "O telefone é obrigatório") @ValidPhone String phone,

        @NotBlank(message = "O CRM é obrigatório") @ValidCRM String crm,

        @NotBlank(message = "A especialidade é obrigatória") String specialty,

        @Valid AddressRequestDTO address) {
}
