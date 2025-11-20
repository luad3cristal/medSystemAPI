package ifba.edu.br.medSystemAPI.dtos.doctor.request;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record DoctorUpdateDTO(
  @NotBlank(message= "Name is required")
  String name, 

  @NotBlank(message= "Phone is required")
  String phone,

  @NotNull(message= "Address is required")
  AddressRequestDTO address
) {
  
  public DoctorUpdateDTO (Doctor doctor) {
    this(
      doctor.getName(), 
      doctor.getPhone(),
      new AddressRequestDTO(doctor.getAddress())
    );
  }

}
