package ifba.edu.br.medSystemAPI.dtos.doctor;

import ifba.edu.br.medSystemAPI.dtos.address.AddressFormDTO;
import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record DoctorFormDTO(
  @NotBlank(message= "Name is required")
  String name, 

  @NotBlank(message= "Email is required")
  @Email
  String email, 

  @NotBlank(message= "Phone is required")
  String phone,

  @NotBlank(message= "CRM is required")
  String crm,

  // Permitir a transformação para Specialty ou converter no construtor da entidade...
  @NotBlank(message= "Specialty is required")
  String specialty,

  @NotNull(message= "Address is required")
  AddressFormDTO address
) {
  
  public DoctorFormDTO (Doctor doctor) {
    this(
      doctor.getName(), 
      doctor.getEmail(), 
      doctor.getPhone(),
      doctor.getCRM(), 
      doctor.getSpecialty().name(), 
      new AddressFormDTO(doctor.getAddress())
    );
  }

}
