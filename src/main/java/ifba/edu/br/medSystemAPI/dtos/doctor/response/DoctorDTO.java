package ifba.edu.br.medSystemAPI.dtos.doctor.response;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
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

  @Schema(description = "Telefone de contato com DDD", example = "71999999999")
  String phone,
  
  @Schema(description = "Número do CRM com UF", example = "12345-BA")
  String crm,

  @Schema(description = "Especialidade médica", example = "CARDIOLOGY")
  Specialty specialty,

  @Schema(description = "Endereço completo do médico")
  AddressRequestDTO address,

  @Schema(description = "Status do médico (ativo/inativo)", example = "true")
  Boolean status
) {
  
  public DoctorDTO (Doctor doctor) {
    this(
      doctor.getId(), 
      doctor.getName(), 
      doctor.getEmail(),
      doctor.getPhone(),
      doctor.getCRM(),
      doctor.getSpecialty(),
      new AddressRequestDTO(doctor.getAddress()),
      doctor.getStatus()
    );
  }

}
