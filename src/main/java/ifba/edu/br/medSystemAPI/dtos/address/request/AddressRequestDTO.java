package ifba.edu.br.medSystemAPI.dtos.address.request;

import ifba.edu.br.medSystemAPI.models.entities.Address;
import jakarta.validation.constraints.NotBlank;


public record AddressRequestDTO(
  @NotBlank(message= "Street is required")
  String street, 
  
  String number, 
  
  String complement, 
  
  @NotBlank(message= "Neighborhood is required")
  String neighborhood,
  
  @NotBlank(message= "City is required")
  String city, 
  
  @NotBlank(message= "State is required")
  String state, 
  
  @NotBlank(message= "ZipCode is required")
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
