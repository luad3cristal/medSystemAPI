package ifba.edu.br.medSystemAPI.dtos.patient.response;

import ifba.edu.br.medSystemAPI.models.entities.Patient;

public record PatientDTO(
  Long id, 
  String name, 
  String email, 
  String cpf
) {
  
  public PatientDTO (Patient patient) {
    // TODO: Tratar NullPointerException se patient for null
    this(
      patient.getId(), 
      patient.getName(), 
      patient.getEmail(),
      patient.getCPF()
    );
  }

}
