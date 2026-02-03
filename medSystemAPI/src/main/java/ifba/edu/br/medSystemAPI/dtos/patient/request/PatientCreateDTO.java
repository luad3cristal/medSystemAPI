package ifba.edu.br.medSystemAPI.dtos.patient.request;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.models.entities.Patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastro de novo paciente")
public record PatientCreateDTO(

  @NotBlank(message= "Name is required")
  @Schema(
    description = "Nome completo do paciente",
    example = "Ana Paula Costa"
  )
  String name, 

  @NotBlank(message= "Email is required")
  @Email
  @Schema(
    description = "Email do paciente (deve ser único no sistema)",
    example = "ana.costa@email.com"
  )
  String email, 
  
  @NotBlank(message= "Phone is required")
  @Schema(
    description = "Telefone de contato com DDD",
    example = "71987651234"
  )
  String phone,

  @NotBlank(message= "CPF is required")
  @CPF
  @Schema(
    description = "CPF do paciente no formato XXX.XXX.XXX-XX (deve ser único e válido)",
    example = "123.456.789-00"
  )
  String cpf,

  @NotNull(message= "Address is required")
  @Schema(
    description = "Endereço completo do paciente"
  )
  AddressRequestDTO address
) {
  
  public PatientCreateDTO (Patient patient) {
    this(
      patient.getName(), 
      patient.getEmail(),
      patient.getPhone(),
      patient.getCPF(),
      new AddressRequestDTO(patient.getAddress())
    );
  }

}
