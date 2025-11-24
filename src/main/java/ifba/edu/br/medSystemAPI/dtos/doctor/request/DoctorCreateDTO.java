package ifba.edu.br.medSystemAPI.dtos.doctor.request;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record DoctorCreateDTO(
  @NotBlank(message= "Name is required")
  String name, 

  @NotBlank(message= "Email is required")
  @Email
  String email, 

  @NotBlank(message= "Phone is required")
  String phone,

  @NotBlank(message= "CRM is required")
  String crm,

  @NotBlank(message= "Specialty is required")
  String specialty,

  @NotNull(message= "Address is required")
  AddressRequestDTO address
) {
  
  public DoctorCreateDTO (Doctor doctor) {
    this(
      doctor.getName(), 
      doctor.getEmail(), 
      doctor.getPhone(),
      doctor.getCRM(), 
      doctor.getSpecialty().name(),
      new AddressRequestDTO(doctor.getAddress())
    );
  }

}
