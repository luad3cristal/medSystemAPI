package ifba.edu.br.medSystemAPI.dtos.auth.request;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.validators.ValidPhone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record PatientRegisterDTO(
        @NotBlank(message = "O email é obrigatório") @Email(message = "Email inválido") String email,

        @NotBlank(message = "A senha é obrigatória") @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String password,

        @NotBlank(message = "O nome é obrigatório") String name,

        @NotBlank(message = "O telefone é obrigatório") @ValidPhone String phone,

        @NotBlank(message = "O CPF é obrigatório") @CPF(message = "CPF inválido") String cpf,

        @Valid AddressRequestDTO address) {
}
