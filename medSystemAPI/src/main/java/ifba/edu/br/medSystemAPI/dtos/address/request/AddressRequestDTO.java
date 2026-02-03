package ifba.edu.br.medSystemAPI.dtos.address.request;

import ifba.edu.br.medSystemAPI.models.entities.Address;
import ifba.edu.br.medSystemAPI.shared.validators.ValidCEP;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de endereço completo")
public record AddressRequestDTO(

    @NotBlank(message = "Street is required") @Schema(description = "Nome da rua/logradouro", example = "Rua das Flores") String street,

    @Schema(description = "Número do imóvel (opcional)", example = "100", nullable = true) String number,

    @Schema(description = "Complemento do endereço (opcional)", example = "Apto 201", nullable = true) String complement,

    @NotBlank(message = "Neighborhood is required") @Schema(description = "Bairro", example = "Centro") String neighborhood,

    @NotBlank(message = "City is required") @Schema(description = "Cidade", example = "Salvador") String city,

    @NotBlank(message = "State is required") @Size(min = 2, max = 2, message = "Estado deve ter exatamente 2 letras (UF)") @Schema(description = "Sigla do estado brasileiro (UF - 2 letras maiúsculas)", example = "BA", maxLength = 2, minLength = 2) String state,

    @NotBlank(message = "ZipCode is required") @ValidCEP @Schema(description = "CEP no formato 00000-000 (com ou sem hífen)", example = "40000-000", pattern = "\\d{5}-?\\d{3}") String zipCode) {
  public AddressRequestDTO(Address address) {
    this(
        address.getStreet(),
        address.getNumber(),
        address.getComplement(),
        address.getNeighborhood(),
        address.getCity(),
        address.getState(),
        address.getZipCode());
  }
}
