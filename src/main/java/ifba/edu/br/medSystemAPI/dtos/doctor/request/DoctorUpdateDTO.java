package ifba.edu.br.medSystemAPI.dtos.doctor.request;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualização de médico (não permite alterar email, CRM e especialidade)")
public record DoctorUpdateDTO(
  
  @NotBlank(message= "Name is required")
  @Schema(
    description = "Nome completo do médico",
    example = "Dr. João Silva Atualizado"
  )
  String name, 

  @NotBlank(message= "Phone is required")
  @Schema(
    description = "Telefone de contato com DDD",
    example = "71999999999"
  )
  String phone,

  @NotNull(message= "Address is required")
  @Schema(
    description = "Endereço completo atualizado do médico"
  )
  AddressRequestDTO address
) {
  
  public DoctorUpdateDTO (Doctor doctor) {
    this(
      doctor.getName(), 
      doctor.getPhone(),
      new AddressRequestDTO(doctor.getAddress())
    );
  }

}
