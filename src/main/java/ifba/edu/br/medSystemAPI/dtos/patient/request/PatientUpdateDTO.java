package ifba.edu.br.medSystemAPI.dtos.patient.request;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.models.entities.Patient;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualização de paciente (não permite alterar email e CPF)")
public record PatientUpdateDTO(
  
  @NotBlank(message= "Name is required")
  @Schema(
    description = "Nome completo do paciente",
    example = "Ana Paula Costa Silva"
  )
  String name, 

  @NotBlank(message= "Phone is required")
  @Schema(
    description = "Telefone de contato com DDD",
    example = "71999998888"
  )
  String phone,

  @NotNull(message= "Address is required")
  @Schema(
    description = "Endereço completo atualizado do paciente"
  )
  AddressRequestDTO address
) {
  
  public PatientUpdateDTO (Patient patient) {
    this(
      patient.getName(), 
      patient.getPhone(),
      new AddressRequestDTO(patient.getAddress())
    );
  }

}
