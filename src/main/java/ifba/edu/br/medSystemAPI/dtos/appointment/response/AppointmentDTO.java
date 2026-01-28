package ifba.edu.br.medSystemAPI.dtos.appointment.response;

import java.time.LocalDateTime;

import ifba.edu.br.medSystemAPI.dtos.doctor.response.DoctorDTO;
import ifba.edu.br.medSystemAPI.dtos.patient.response.PatientDTO;
import ifba.edu.br.medSystemAPI.models.entities.Appointment;
import ifba.edu.br.medSystemAPI.models.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record AppointmentDTO(
  
  @Schema(description = "ID único da consulta", example = "1")
  Long id,
  
  @Schema(description = "Dados do paciente")
  PatientDTO patient,
  
  @Schema(description = "Dados do médico")
  DoctorDTO doctor,
  // TODO: mostrar com o campo como "data que a marcação foi feita" ou algo assim...
  @Schema(
    description = "Data e hora de criação da consulta",
    example = "2025-11-30T10:30:00",
    type = "string",
    format = "date-time"
  )  
  LocalDateTime createdAt,

  @Schema(
    description = "Data e hora da consulta",
    example = "2025-12-16T14:00:00",
    type = "string",
    format = "date-time"
  )  
  LocalDateTime appointmentTime,

  @Schema(
    description = "Status da consulta",
    example = "SCHEDULED",
    allowableValues = {"SCHEDULED", "CANCELLED", "COMPLETED"}
  )
  AppointmentStatus status,
  
  @Schema(
    description = "Data e hora de cancelamento da consulta",
    example = "2025-11-30T10:30:00",
    type = "string",
    format = "date-time"
  )    
  LocalDateTime cancelledAt, 
    
  @Schema(
    description = "Motivo do cancelamento (null se não cancelada)",
    nullable = true
  )
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
