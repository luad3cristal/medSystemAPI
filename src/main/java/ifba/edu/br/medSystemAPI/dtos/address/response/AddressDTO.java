package ifba.edu.br.medSystemAPI.dtos.address.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import ifba.edu.br.medSystemAPI.models.entities.Address;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados de resposta de endereço")
public record AddressDTO(
  
  @Schema(description = "ID único do endereço", example = "1")
  Long id,
  
  @Schema(description = "Nome da rua/logradouro", example = "Rua das Flores")
  String street, 
  
  @Schema(description = "Número do imóvel", example = "100")
  String number, 
  
  @Schema(description = "Complemento", example = "Sala 201")
  String complement, 
  
  @Schema(description = "Bairro", example = "Centro")
  String neighborhood,
  
  @Schema(description = "Cidade", example = "Salvador")
  String city, 
  
  @Schema(description = "UF do estado", example = "BA")
  String state, 
  
  @Schema(description = "CEP", example = "40000-000")
  String zipCode
) {
  public AddressDTO (Address address) {
    this(
      address.getId(), 
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
