package ifba.edu.br.medSystemAPI.dtos.address.request;

import ifba.edu.br.medSystemAPI.models.entities.Address;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de endereço completo")
public record AddressRequestDTO(
  
  @NotBlank(message= "Street is required")
  @Schema(
      description = "Nome da rua/logradouro",
      example = "Rua das Flores"
  )
  String street, 
  
  @Schema(
      description = "Número do imóvel (opcional)",
      example = "100",
      nullable = true
  )
  String number, 
  
  @Schema(
      description = "Complemento do endereço (opcional)",
      example = "Sala 201",
      nullable = true
  )
  String complement, 
  
  @NotBlank(message= "Neighborhood is required")
  @Schema(
      description = "Bairro",
      example = "Centro"
  )
  String neighborhood,
  
  @NotBlank(message= "City is required")
  @Schema(
      description = "Cidade",
      example = "Salvador"
  )
  String city, 
  
  @NotBlank(message= "State is required")
  @Schema(
      description = "UF do estado (2 letras)",
      example = "BA",
      maxLength = 2
  )
  String state, 
  
  @NotBlank(message= "ZipCode is required")
  @Schema(
      description = "CEP no formato XXXXX-XXX",
      example = "40000-000",
      pattern = "\\d{5}-\\d{3}"
  )
  String zipCode
) {
  public AddressRequestDTO (Address address) {
    this(
      address.getStreet(), 
      address.getNumber(),
      address.getComplement(),
      address.getNeighborhood(),
      address.getCity(), 
      address.getState(),
      address.getZipCode()
    );
  }
}
