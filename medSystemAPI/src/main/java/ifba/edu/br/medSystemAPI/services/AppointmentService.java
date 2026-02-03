package ifba.edu.br.medSystemAPI.services;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ifba.edu.br.medSystemAPI.dtos.appointment.request.AppointmentCancelDTO;
import ifba.edu.br.medSystemAPI.dtos.appointment.request.AppointmentCreateDTO;
import ifba.edu.br.medSystemAPI.dtos.appointment.response.AppointmentDTO;
import ifba.edu.br.medSystemAPI.models.entities.Appointment;
import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import ifba.edu.br.medSystemAPI.models.entities.Patient;
import ifba.edu.br.medSystemAPI.models.entities.User;
import ifba.edu.br.medSystemAPI.models.enums.AppointmentStatus;
import ifba.edu.br.medSystemAPI.models.enums.Role;
import ifba.edu.br.medSystemAPI.repositories.AppointmentRepository;
import ifba.edu.br.medSystemAPI.repositories.DoctorRepository;
import ifba.edu.br.medSystemAPI.repositories.PatientRepository;
import ifba.edu.br.medSystemAPI.shared.exceptions.AccessDeniedException;
import ifba.edu.br.medSystemAPI.shared.exceptions.appointment.AppointmentCannotBeCancelledException;
import ifba.edu.br.medSystemAPI.shared.exceptions.appointment.AppointmentCannotBeCompletedException;
import ifba.edu.br.medSystemAPI.shared.exceptions.appointment.AppointmentNotFoundException;
import ifba.edu.br.medSystemAPI.shared.exceptions.appointment.DoctorNotAvailableException;
import ifba.edu.br.medSystemAPI.shared.exceptions.appointment.InvalidAppointmentTimeException;
import ifba.edu.br.medSystemAPI.shared.exceptions.appointment.PatientAlreadyHasAppointmentException;
import ifba.edu.br.medSystemAPI.shared.exceptions.patient.PatientNotFoundException;
import ifba.edu.br.medSystemAPI.shared.utils.ValidationUtils;

@Service
public class AppointmentService {

  public PatientRepository patientRepository;
  public DoctorRepository doctorRepository;
  public AppointmentRepository appointmentRepository;

  public AppointmentService(PatientRepository patientRepository, DoctorRepository doctorRepository,
      AppointmentRepository appointmentRepository) {
    this.patientRepository = patientRepository;
    this.doctorRepository = doctorRepository;
    this.appointmentRepository = appointmentRepository;
  }

  private Doctor selectRandomDoctor(LocalDateTime appointmentTime) {
    List<Doctor> allDoctors = doctorRepository.findByUserEnabledTrue()
        .stream()
        .filter(this::isDoctorActive)
        .filter(doctor -> !isDoctorBusy(doctor, appointmentTime))
        .filter(doctor -> isDoctorAvailableForOneHour(doctor, appointmentTime))
        .toList();

    if (allDoctors.isEmpty()) {
      throw new DoctorNotAvailableException("No doctors available at " + appointmentTime);
    }

    List<Doctor> doctorsList = new ArrayList<>(allDoctors);
    Collections.shuffle(doctorsList);
    return doctorsList.get(0);
  }

  private boolean isValidBusinessHour(LocalDateTime appointmentTime) {
    boolean isSunday = appointmentTime.getDayOfWeek() != DayOfWeek.SUNDAY;

    LocalTime start = LocalTime.of(7, 0);
    LocalTime end = LocalTime.of(19, 0);
    LocalTime time = appointmentTime.toLocalTime();
    boolean isBusinessHour = !time.isBefore(start) && time.isBefore(end);
    return isSunday && isBusinessHour;
  }

  private boolean hasMinimumAdvanceTime(LocalDateTime appointmentTime) {
    return Duration.between(LocalDateTime.now(), appointmentTime).toMinutes() >= 30;
  }

  private boolean isPatientActive(Patient patient) {
    return patient.getStatus();
  }

  private boolean isDoctorActive(Doctor doctor) {
    return doctor.getStatus();
  }

  private boolean isDoctorBusy(Doctor doctor, LocalDateTime appointmentTime) {
    return appointmentRepository.existsByDoctorAndAppointmentTime(doctor, appointmentTime);
  }

  private boolean isDoctorAvailableForOneHour(Doctor doctor, LocalDateTime startTime) {
    LocalDateTime endTime = startTime.plusHours(1);
    List<Appointment> doctorsScheduledAppointments = appointmentRepository
        .findByDoctorAndAppointmentTimeBetween(doctor, startTime, endTime)
        .stream()
        .filter(
            appointment -> appointment.getStatus() == AppointmentStatus.SCHEDULED)
        .toList();
    ;

    return doctorsScheduledAppointments.isEmpty();
  }

  private boolean hasAppointmentSameDay(Patient patient, LocalDateTime appointmentTime) {
    LocalDateTime starDate = appointmentTime.toLocalDate().atStartOfDay();
    LocalDateTime endDate = appointmentTime.toLocalDate().atTime(23, 59, 59);
    List<Appointment> patientsScheduledAppointments = appointmentRepository
        .findByPatientAndAppointmentTimeBetween(patient, starDate, endDate)
        .stream()
        .filter(
            appointment -> appointment.getStatus() == AppointmentStatus.SCHEDULED)
        .toList();

    return !patientsScheduledAppointments.isEmpty();
  }

  private boolean canBeCancelled(Appointment appointment) {
    boolean isScheduled = appointment.getStatus() == AppointmentStatus.SCHEDULED;
    boolean hasMinimum24Hours = Duration.between(LocalDateTime.now(), appointment.getAppointmentTime()).toHours() >= 24;
    return isScheduled && hasMinimum24Hours;
  }

  private void validateSchedulingRules(Patient patient, Doctor doctor, LocalDateTime appointmentTime) {

    if (!isValidBusinessHour(appointmentTime)) {
      throw new InvalidAppointmentTimeException(
          "Appointments must be scheduled Monday to Saturday, between 07:00 and 19:00");
    }

    if (!hasMinimumAdvanceTime(appointmentTime)) {
      throw new InvalidAppointmentTimeException("Appointments must be scheduled at least 30 minutes in advance");
    }

    if (!isPatientActive(patient)) {
      throw new InvalidAppointmentTimeException("This patient is inactive");
    }

    if (!isDoctorActive(doctor)) {
      throw new InvalidAppointmentTimeException("This doctor is inactive");
    }

    if (isDoctorBusy(doctor, appointmentTime)) {
      throw new DoctorNotAvailableException(doctor, appointmentTime);
    }

    if (!isDoctorAvailableForOneHour(doctor, appointmentTime)) {
      throw new DoctorNotAvailableException("Doctor not available for the full hour duration");
    }

    if (hasAppointmentSameDay(patient, appointmentTime)) {
      throw new PatientAlreadyHasAppointmentException(patient, appointmentTime.toLocalDate());
    }

  }

  private void validateCancellationRules(Appointment appointment) {
    if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
      throw new AppointmentCannotBeCancelledException(appointment.getId(),
          "Only scheduled appointments can be cancelled");
    }

    if (!canBeCancelled(appointment)) {
      throw new AppointmentCannotBeCancelledException(appointment.getId(),
          "Must be cancelled at least 24 hours in advance");
    }

  }

  private void validateCompletionRules(Appointment appointment) {
    if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
      throw new AppointmentCannotBeCompletedException(appointment.getId(),
          "Only scheduled appointments can be completed");
    }
  }

  public AppointmentDTO getAppointmentById(Long id) {
    return new AppointmentDTO(
        this.appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id)));
  }

  public Page<AppointmentDTO> getAllAppointments(Pageable pageable, AppointmentStatus status) {
    if (status == null) {
      return this.appointmentRepository.findAll(pageable).map(AppointmentDTO::new);
    }

    return this.appointmentRepository.findByStatus(status, pageable)
        .map(AppointmentDTO::new);
  }

  public AppointmentDTO scheduleAppointment(AppointmentCreateDTO appointment) {
    Patient patientInfo = this.patientRepository.findById(appointment.patientId())
        .orElseThrow(() -> new PatientNotFoundException(appointment.patientId()));

    Doctor doctorInfo = findDoctor(appointment);

    validateSchedulingRules(patientInfo, doctorInfo, appointment.appointmentTime());

    Appointment newAppointment = new Appointment(patientInfo, doctorInfo, appointment.appointmentTime(),
        LocalDateTime.now());

    return new AppointmentDTO(this.appointmentRepository.save(newAppointment));
  }

  /**
   * Busca o médico baseado em CRM, Nome ou seleciona aleatoriamente
   */
  private Doctor findDoctor(AppointmentCreateDTO appointment) {
    // Se todos os campos estão null, seleciona aleatoriamente
    if (appointment.doctorCrm() == null &&
        appointment.doctorName() == null) {
      return selectRandomDoctor(appointment.appointmentTime());
    }

    // Prioridade 1: Buscar por CRM
    if (appointment.doctorCrm() != null && !appointment.doctorCrm().trim().isEmpty()) {
      if (!ValidationUtils.isValidCRM(appointment.doctorCrm())) {
        throw new DoctorNotAvailableException(
            "CRM inválido: " + appointment.doctorCrm() + ". Formato esperado: CRM/UF 000000 (ex: CRM/SP 123456)");
      }
      return this.doctorRepository.findByCrmAndUserEnabledTrue(appointment.doctorCrm())
          .orElseThrow(
              () -> new DoctorNotAvailableException("Médico não encontrado com CRM: " + appointment.doctorCrm()));
    }

    // Prioridade 2: Buscar por Nome
    if (appointment.doctorName() != null && !appointment.doctorName().trim().isEmpty()) {
      List<Doctor> doctors = this.doctorRepository.findByNameContainingIgnoreCaseAndUserEnabledTrue(
          appointment.doctorName());

      if (doctors.isEmpty()) {
        throw new DoctorNotAvailableException("Nenhum médico encontrado com nome: " + appointment.doctorName());
      }

      if (doctors.size() > 1) {
        String doctorNames = doctors.stream()
            .map(d -> d.getName() + " (CRM: " + d.getCRM() + ")")
            .reduce((a, b) -> a + ", " + b)
            .orElse("");
        throw new DoctorNotAvailableException(
            "Múltiplos médicos encontrados com nome '" + appointment.doctorName() +
                "'. Por favor, especifique o CRM. Médicos encontrados: " + doctorNames);
      }

      return doctors.get(0);
    }

    // Fallback: seleciona aleatoriamente
    return selectRandomDoctor(appointment.appointmentTime());
  }

  public AppointmentDTO cancelAppointment(Long id, AppointmentCancelDTO appointment) {
    Appointment existingAppointment = appointmentRepository.findById(id)
        .orElseThrow(() -> new AppointmentNotFoundException(id));

    validateCancellationRules(existingAppointment);

    existingAppointment.setStatus(AppointmentStatus.CANCELLED);
    existingAppointment.setCancelledReason(appointment.cancelledReason());
    existingAppointment.setCancelledTime(LocalDateTime.now());

    return new AppointmentDTO(this.appointmentRepository.save(existingAppointment));
  }

  public AppointmentDTO completeAppointment(Long id) {
    Appointment existingAppointment = appointmentRepository.findById(id)
        .orElseThrow(() -> new AppointmentNotFoundException(id));

    validateCompletionRules(existingAppointment);

    existingAppointment.setStatus(AppointmentStatus.COMPLETED);

    return new AppointmentDTO(this.appointmentRepository.save(existingAppointment));
  }

  /**
   * Busca todas as consultas de um paciente específico (paginado)
   * Valida se o usuário tem permissão para acessar as consultas
   */
  public Page<AppointmentDTO> getPatientAppointments(Long patientId, Pageable pageable, AppointmentStatus status) {
    Patient patient = patientRepository.findById(patientId)
        .orElseThrow(() -> new PatientNotFoundException(patientId));

    // Validar acesso: apenas o próprio paciente ou ADMIN pode ver
    validatePatientAccess(patient);

    if (status == null) {
      return appointmentRepository.findByPatient(patient, pageable)
        .map(AppointmentDTO::new);
    }

    return appointmentRepository.findByPatientAndStatus(patient, status, pageable)
      .map(AppointmentDTO::new);
  }

  /**
   * Busca todas as consultas de um médico específico (paginado)
   * Valida se o usuário tem permissão para acessar as consultas
   */
  public Page<AppointmentDTO> getDoctorAppointments(Long doctorId, Pageable pageable, AppointmentStatus status) {
    Doctor doctor = doctorRepository.findById(doctorId)
        .orElseThrow(() -> new ifba.edu.br.medSystemAPI.shared.exceptions.doctor.DoctorNotFoundException(doctorId));

    // Validar acesso: apenas o próprio médico ou ADMIN pode ver
    validateDoctorAccess(doctor);

    if (status == null) {
      return appointmentRepository.findByDoctor(doctor, pageable)
        .map(AppointmentDTO::new);
    }

    return appointmentRepository.findByDoctorAndStatus(doctor, status, pageable)
      .map(AppointmentDTO::new);
  }

  /**
   * Valida se o usuário autenticado tem permissão para acessar dados do paciente
   * Permite acesso apenas ao próprio paciente ou ADMIN
   */
  private void validatePatientAccess(Patient patient) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal();

    // ADMIN pode acessar tudo
    if (currentUser.getRole() == Role.ADMIN) {
      return;
    }

    // PATIENT pode acessar apenas suas próprias consultas
    if (currentUser.getRole() == Role.PATIENT) {
      if (currentUser.getPatient() == null || !currentUser.getPatient().getId().equals(patient.getId())) {
        throw new AccessDeniedException("Você só pode acessar suas próprias consultas");
      }
      return;
    }

    // Outros roles não têm acesso
    throw new AccessDeniedException("Acesso negado às consultas deste paciente");
  }

  /**
   * Valida se o usuário autenticado tem permissão para acessar dados do médico
   * Permite acesso apenas ao próprio médico ou ADMIN
   */
  private void validateDoctorAccess(Doctor doctor) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal();

    // ADMIN pode acessar tudo
    if (currentUser.getRole() == Role.ADMIN) {
      return;
    }

    // DOCTOR pode acessar apenas suas próprias consultas
    if (currentUser.getRole() == Role.DOCTOR) {
      if (currentUser.getDoctor() == null || !currentUser.getDoctor().getId().equals(doctor.getId())) {
        throw new AccessDeniedException("Você só pode acessar suas próprias consultas");
      }
      return;
    }

    // Outros roles não têm acesso
    throw new AccessDeniedException("Acesso negado às consultas deste médico");
  }

}