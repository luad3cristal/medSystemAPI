package ifba.edu.br.medSystemAPI.dtos.patient;

import ifba.edu.br.medSystemAPI.models.entities.Patient;

public record PatientDTO(
  Long id, 
  String name, 
  String email, 
  String cpf
) {
  
  public PatientDTO (Patient patient) {
    this(
      patient.getId(), 
      patient.getName(), 
      patient.getEmail(),
      patient.getCPF()
    );
  }

}
