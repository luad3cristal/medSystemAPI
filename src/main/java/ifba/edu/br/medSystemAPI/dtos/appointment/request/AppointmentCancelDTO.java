package ifba.edu.br.medSystemAPI.dtos.appointment.request;

import ifba.edu.br.medSystemAPI.models.entities.Appointment;
import jakarta.validation.constraints.NotNull;

public record AppointmentCancelDTO(  
  @NotNull(message= "Reason to cancel is required")
  String cancelledReason
) {
  
    public AppointmentCancelDTO(Appointment appointment) {
    this(
      appointment.getCancelledReason()
    );
  }

}
