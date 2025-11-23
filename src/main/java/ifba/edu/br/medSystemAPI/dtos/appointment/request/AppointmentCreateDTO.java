package ifba.edu.br.medSystemAPI.dtos.appointment.request;

import java.time.LocalDateTime;

import ifba.edu.br.medSystemAPI.models.entities.Appointment;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record AppointmentCreateDTO(
  @NotNull(message= "Patient ID is required")
  Long patientId,
  
  Long doctorId,
  
  @NotNull(message= "Appointment date and time is required")
  @Future(message = "Appointment must be scheduled for a future date")
  LocalDateTime appointmentTime
) {
  
  public AppointmentCreateDTO(Appointment appointment) {
    this(
      appointment.getPatient().getId(), 
      appointment.getDoctor().getId(), 
      appointment.getAppointmentTime()
    );
  }

}
