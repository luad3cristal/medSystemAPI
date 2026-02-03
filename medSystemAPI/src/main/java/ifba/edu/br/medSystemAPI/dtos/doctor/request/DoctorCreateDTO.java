package ifba.edu.br.medSystemAPI.dtos.doctor.request;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastro de novo médico")
public record DoctorCreateDTO(
  
  @NotBlank(message= "Name is required")
  @Schema(
    description = "Nome completo do médico",
    example = "Dr. João Silva"
  )
  String name, 

  @NotBlank(message= "Email is required")
  @Email
  @Schema(
    description = "Email profissional do médico (deve ser único no sistema)",
    example = "joao.silva@hospital.com"
  )
  String email, 

  @NotBlank(message= "Phone is required")
  @Schema(
    description = "Telefone de contato com DDD",
    example = "71987654321"
  )
  String phone,

  @NotBlank(message= "CRM is required")
  @Schema(
    description = "Número do CRM com UF (deve ser único no sistema)",
    example = "12345-BA"
  )
  String crm,

  @NotBlank(message= "Specialty is required")
  @Schema(
    description = "Especialidade médica",
    example = "CARDIOLOGY",
    allowableValues = {"CARDIOLOGY", "ORTHOPEDICS", "GYNECOLOGY", "DERMATOLOGY"}
  )
  String specialty,

  @NotNull(message= "Address is required")
  @Schema(
    description = "Endereço completo do médico"
  )
  AddressRequestDTO address
) {
  
  public DoctorCreateDTO (Doctor doctor) {
    this(
      doctor.getName(), 
      doctor.getEmail(), 
      doctor.getPhone(),
      doctor.getCRM(), 
      doctor.getSpecialty().name(),
      new AddressRequestDTO(doctor.getAddress())
    );
  }

}
