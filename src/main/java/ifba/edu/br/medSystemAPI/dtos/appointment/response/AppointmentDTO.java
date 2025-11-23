package ifba.edu.br.medSystemAPI.dtos.appointment.response;

import java.time.LocalDateTime;

import ifba.edu.br.medSystemAPI.dtos.doctor.response.DoctorDTO;
import ifba.edu.br.medSystemAPI.dtos.patient.response.PatientDTO;
import ifba.edu.br.medSystemAPI.models.entities.Appointment;
import ifba.edu.br.medSystemAPI.models.enums.AppointmentStatus;

public record AppointmentDTO(
  Long id,
  PatientDTO patient, 
  DoctorDTO doctor,
  // mostrar com o campo como "data que a marcação foi feita" ou algo assim...
  LocalDateTime createdAt,
  LocalDateTime appointmentTime,
  AppointmentStatus status,
  LocalDateTime cancelledAt, 
  String cancelledReason
) {
  public AppointmentDTO (Appointment appointment) {
    this(
      appointment.getId(),
      new PatientDTO(appointment.getPatient()),
      new DoctorDTO(appointment.getDoctor()),
      appointment.getCreatedTime(),
      appointment.getAppointmentTime(),
      appointment.getStatus(),
      appointment.getCancelledTime(),
      appointment.getCancelledReason()
    );
  }
  
}
