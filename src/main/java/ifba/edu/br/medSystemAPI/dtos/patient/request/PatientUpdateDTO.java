package ifba.edu.br.medSystemAPI.dtos.patient.request;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.models.entities.Patient;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record PatientUpdateDTO(
  @NotBlank(message= "Name is required")
  String name, 

  @NotBlank(message= "Phone is required")
  String phone,

  @NotNull(message= "Address is required")
  AddressRequestDTO address
) {
  
  public PatientUpdateDTO (Patient patient) {
    this(
      patient.getName(), 
      patient.getPhone(),
      new AddressRequestDTO(patient.getAddress())
    );
  }

}
