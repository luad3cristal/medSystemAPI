package ifba.edu.br.medSystemAPI.dtos.appointment.request;

import ifba.edu.br.medSystemAPI.models.entities.Appointment;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cancelamento de consulta")
public record AppointmentCancelDTO(
  
  @NotNull(message= "Reason to cancel is required")
  @Schema(
    description = "Motivo do cancelamento da consulta",
    example = "Paciente não poderá comparecer por motivos pessoais"
  )
  String cancelledReason
) {
  
    public AppointmentCancelDTO(Appointment appointment) {
    this(
      appointment.getCancelledReason()
    );
  }

}
