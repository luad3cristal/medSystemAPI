package ifba.edu.br.medSystemAPI.dtos.doctor;

import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import ifba.edu.br.medSystemAPI.models.enums.Specialty;

public record DoctorDTO(
  Long id, 
  String name, 
  String email, 
  String crm, 
  Specialty specialty
) {
  
  public DoctorDTO (Doctor doctor) {
    this(
      doctor.getId(), 
      doctor.getName(), 
      doctor.getEmail(),
      doctor.getCRM(),
      doctor.getSpecialty()
    );
  }

}
