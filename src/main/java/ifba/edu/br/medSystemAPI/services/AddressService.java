package ifba.edu.br.medSystemAPI.services;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.models.entities.Address;

public class AddressService {
  
  public void updateAddress(Address existingAddress, AddressRequestDTO address) {
    existingAddress.setStreet(address.street());
      existingAddress.setNumber(address.number());
      existingAddress.setComplement(address.complement());
      existingAddress.setNeighborhood(address.neighborhood());
      existingAddress.setCity(address.city());
      existingAddress.setState(address.state());
      existingAddress.setZipCode(address.zipCode());
  }

  public Address createAddress (AddressRequestDTO address) {
    return new Address(address);
  }

}
