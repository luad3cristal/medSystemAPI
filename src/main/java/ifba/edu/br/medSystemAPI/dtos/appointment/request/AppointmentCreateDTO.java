package ifba.edu.br.medSystemAPI.dtos.appointment.request;

import java.time.LocalDateTime;

import ifba.edu.br.medSystemAPI.models.entities.Appointment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para agendamento de consulta")
public record AppointmentCreateDTO(
  
  @NotNull(message = "Patient ID is required")
  @Schema(
    description = "ID do paciente que será atendido",
    example = "1"
  )
  Long patientId,
  
  @Schema(
    description = "ID do médico (opcional - se null, o sistema seleciona um médico aleatório disponível)",
    example = "1",
    nullable = true
  )
  Long doctorId,
  
  @NotNull(message = "Appointment time is required")
  @Schema(
    description = "Data e hora da consulta no formato ISO 8601. Deve ser em horário comercial (segunda a sábado, 07:00-19:00) com antecedência mínima de 30 minutos",
    example = "2025-12-16T14:00:00",
    type = "string",
    format = "date-time"
  )
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
