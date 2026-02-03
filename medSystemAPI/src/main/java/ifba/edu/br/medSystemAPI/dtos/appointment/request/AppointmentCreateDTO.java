package ifba.edu.br.medSystemAPI.dtos.appointment.request;

import java.time.LocalDateTime;

import ifba.edu.br.medSystemAPI.models.entities.Appointment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para agendamento de consulta")
public record AppointmentCreateDTO(

    @NotNull(message = "Patient ID is required") @Schema(description = "ID do paciente que será atendido", example = "1") Long patientId,

    @Schema(description = "CRM do médico no formato CRM/UF 000000 (ex: CRM/SP 123456). "
        + "OPCIONAL: Pode especificar o CRM, ou o nome do médico, ou deixar ambos null para seleção aleatória de médico disponível.", example = "CRM/SP 123456", nullable = true, pattern = "^CRM/[A-Z]{2}\\s?\\d{4,6}$") String doctorCrm,

    @Schema(description = "Nome do médico para busca. "
        + "OPCIONAL: Pode especificar o nome, ou o CRM, ou deixar ambos null para seleção aleatória de médico disponível. "
        + "Se houver múltiplos médicos com o mesmo nome, será necessário usar o CRM.", example = "Dr. João Silva", nullable = true) String doctorName,

    @NotNull(message = "Appointment time is required") @Schema(description = "Data e hora da consulta no formato ISO 8601. "
        + "REGRAS: Deve ser em horário comercial (segunda a sábado, 07:00-19:00) com antecedência mínima de 30 minutos. "
        + "O paciente não pode ter mais de uma consulta no mesmo dia.", example = "2025-12-16T14:00:00", type = "string", format = "date-time") LocalDateTime appointmentTime
) {

  public AppointmentCreateDTO(Appointment appointment) {
    this(
        appointment.getPatient().getId(),
        appointment.getDoctor().getCRM(),
        appointment.getDoctor().getName(),
        appointment.getAppointmentTime());
  }

}
