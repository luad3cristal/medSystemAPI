package ifba.edu.br.medSystemAPI.dtos.patient.request;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.models.entities.Patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record PatientCreateDTO(
  @NotBlank(message= "Name is required")
  String name, 

  @NotBlank(message= "Email is required")
  @Email
  String email, 
  
  @NotBlank(message= "Phone is required")
  String phone,

  @NotBlank(message= "CPF is required")
  @CPF
  String cpf,

  @NotNull(message= "Address is required")
  AddressRequestDTO address
) {
  
  public PatientCreateDTO (Patient patient) {
    this(
      patient.getName(), 
      patient.getEmail(),
      patient.getPhone(),
      patient.getCPF(),
      new AddressRequestDTO(patient.getAddress())
    );
  }

}
