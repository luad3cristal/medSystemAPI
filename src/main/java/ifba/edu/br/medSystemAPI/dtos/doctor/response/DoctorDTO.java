package ifba.edu.br.medSystemAPI.dtos.doctor.response;

import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import ifba.edu.br.medSystemAPI.models.enums.Specialty;
import io.swagger.v3.oas.annotations.media.Schema;

public record DoctorDTO(
  @Schema(description = "ID único do médico", example = "1")
  Long id,
  
  @Schema(description = "Nome completo do médico", example = "Dr. João Silva")
  String name,
  
  @Schema(description = "Email profissional do médico", example = "joao.silva@hospital.com")
  String email,
  
  @Schema(description = "Número do CRM com UF", example = "12345-BA")
  String crm,

  @Schema(description = "Especialidade médica", example = "CARDIOLOGY")
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
