package ifba.edu.br.medSystemAPI.dtos.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import ifba.edu.br.medSystemAPI.models.entities.Address;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AddressDTO(
  Long id,
  String street, 
  String number, 
  String complement, 
  String neighborhood,
  String city, 
  String state, 
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
