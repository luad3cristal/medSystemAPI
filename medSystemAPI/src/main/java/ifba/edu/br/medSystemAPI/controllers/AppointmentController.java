package ifba.edu.br.medSystemAPI.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifba.edu.br.medSystemAPI.dtos.appointment.request.AppointmentCancelDTO;
import ifba.edu.br.medSystemAPI.dtos.appointment.request.AppointmentCreateDTO;
import ifba.edu.br.medSystemAPI.dtos.appointment.response.AppointmentDTO;
import ifba.edu.br.medSystemAPI.services.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointments")
@Tag(name = "Consultas", description = "Endpoints para gerenciamento de consultas médicas")
@SecurityRequirement(name = "bearer-key")
public class AppointmentController {
  private AppointmentService appointmentService;

  public AppointmentController(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Listar TODAS as consultas (ADMIN apenas)", description = "Retorna uma lista paginada de todas as consultas cadastradas no sistema, ordenadas por data/hora da consulta. Acesso restrito a ADMIN.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de consultas retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "403", description = "Acesso negado - apenas ADMIN pode listar todas as consultas", content = @Content(mediaType = "application/json"))
  })
  public ResponseEntity<Page<AppointmentDTO>> getAppointments(
      @PageableDefault(size = 10, sort = "appointmentTime") Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(appointmentService.getAllAppointments(pageable));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Buscar consulta por ID (ADMIN apenas)", description = "Retorna os detalhes completos de uma consulta específica. Acesso restrito a ADMIN.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Consulta encontrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDTO.class))),
      @ApiResponse(responseCode = "404", description = "Consulta não encontrada com o ID fornecido", content = @Content(mediaType = "application/json"))
  })
  public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(appointmentService.getAppointmentById(id));
  }

  @PostMapping()
  @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
  @Operation(summary = "Agendar consulta (PACIENTE ou ADMIN)", description = "Agenda uma nova consulta médica. Pacientes podem agendar para si mesmos, ADMIN pode agendar para qualquer paciente. "
      + "O médico pode ser especificado por CRM ou nome, ou selecionado aleatoriamente. "
      + "Regras: horário de segunda a sábado das 07:00 às 19:00, antecedência mínima de 30 minutos, "
      + "paciente não pode ter mais de uma consulta no mesmo dia, médico não pode ter conflitos de horário")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Consulta agendada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos (horário fora do expediente, antecedência insuficiente, campos obrigatórios ausentes)", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Paciente ou médico não encontrado", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "409", description = "Conflito de agendamento (médico ocupado, paciente já tem consulta no dia, médico ou paciente inativo)", content = @Content(mediaType = "application/json"))
  })
  public ResponseEntity<AppointmentDTO> scheduleAppointment(@RequestBody @Valid AppointmentCreateDTO appointment) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(appointmentService.scheduleAppointment(appointment));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
  @Operation(summary = "Cancelar consulta (Paciente, Médico ou ADMIN)", description = "Cancela uma consulta agendada, requerindo o motivo do cancelamento e exigindo que seja feito com antecedência mínima de 24 horas. "
      + "Pacientes podem cancelar suas próprias consultas, médicos podem cancelar consultas onde são o médico responsável, ADMIN pode cancelar qualquer consulta.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Consulta cancelada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDTO.class))),
      @ApiResponse(responseCode = "400", description = "Cancelamento inválido (antecedência menor que 24 horas, motivo não fornecido ou consulta já cancelada)", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Consulta não encontrada com o ID fornecido", content = @Content(mediaType = "application/json"))
  })
  public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable Long id,
      @RequestBody @Valid AppointmentCancelDTO appointment) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(appointmentService.cancelAppointment(id, appointment));
  }

  @GetMapping("/patient/{patientId}/my-consultations")
  @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
  @Operation(summary = "Listar consultas do paciente", description = "Retorna todas as consultas de um paciente específico, ordenadas por data/hora. "
      + "Pacientes podem ver apenas suas próprias consultas, ADMIN pode ver de qualquer paciente.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de consultas do paciente retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "403", description = "Acesso negado - paciente tentando acessar consultas de outro paciente", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Paciente não encontrado", content = @Content(mediaType = "application/json"))
  })
  public ResponseEntity<Page<AppointmentDTO>> getPatientAppointments(
      @PathVariable Long patientId,
      @PageableDefault(size = 10, sort = "appointmentTime") Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(appointmentService.getPatientAppointments(patientId, pageable));
  }

  @GetMapping("/doctor/{doctorId}/my-consultations")
  @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
  @Operation(summary = "Listar consultas do médico", description = "Retorna todas as consultas de um médico específico, ordenadas por data/hora. "
      + "Médicos podem ver apenas suas próprias consultas, ADMIN pode ver de qualquer médico.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de consultas do médico retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "403", description = "Acesso negado - médico tentando acessar consultas de outro médico", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Médico não encontrado", content = @Content(mediaType = "application/json"))
  })
  public ResponseEntity<Page<AppointmentDTO>> getDoctorAppointments(
      @PathVariable Long doctorId,
      @PageableDefault(size = 10, sort = "appointmentTime") Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(appointmentService.getDoctorAppointments(doctorId, pageable));
  }

}
