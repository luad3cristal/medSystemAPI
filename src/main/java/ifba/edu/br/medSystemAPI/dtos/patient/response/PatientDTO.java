package ifba.edu.br.medSystemAPI.dtos.patient.response;

import ifba.edu.br.medSystemAPI.models.entities.Patient;
import io.swagger.v3.oas.annotations.media.Schema;

public record PatientDTO(

  @Schema(description = "ID único do paciente", example = "1")
  Long id,
  
  @Schema(description = "Nome completo do paciente", example = "Ana Paula Costa")
  String name,
  
  @Schema(description = "Email do paciente", example = "ana.costa@email.com")
  String email,
  
  @Schema(description = "CPF do paciente", example = "123.456.789-00")
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
