package ifba.edu.br.medSystemAPI.dtos.patient.response;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.models.entities.Patient;
import io.swagger.v3.oas.annotations.media.Schema;

public record PatientDTO(

  @Schema(description = "ID único do paciente", example = "1")
  Long id,
  
  @Schema(description = "Nome completo do paciente", example = "Ana Paula Costa")
  String name,
  
  @Schema(description = "Email do paciente", example = "ana.costa@email.com")
  String email,

  @Schema(description = "Telefone de contato com DDD", example = "71999998888")
  String phone,
  
  @Schema(description = "CPF do paciente", example = "123.456.789-00")
  String cpf,

  @Schema(description = "Endereço completo do paciente")
  AddressRequestDTO address,

  @Schema(description = "Status do paciente (ativo/inativo)", example = "true")
  Boolean status
) {
  
  public PatientDTO (Patient patient) {
    this(
      patient.getId(), 
      patient.getName(), 
      patient.getEmail(),
      patient.getPhone(),
      patient.getCPF(),
      new AddressRequestDTO(patient.getAddress()),
      patient.getStatus()
    );
  }

}
