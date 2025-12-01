package ifba.edu.br.medSystemAPI.services;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import ifba.edu.br.medSystemAPI.dtos.appointment.request.AppointmentCancelDTO;
import ifba.edu.br.medSystemAPI.dtos.appointment.request.AppointmentCreateDTO;
import ifba.edu.br.medSystemAPI.dtos.appointment.response.AppointmentDTO;
import ifba.edu.br.medSystemAPI.exceptions.appointment.AppointmentCannotBeCancelledException;
import ifba.edu.br.medSystemAPI.exceptions.appointment.AppointmentNotFoundException;
import ifba.edu.br.medSystemAPI.exceptions.appointment.DoctorNotAvailableException;
import ifba.edu.br.medSystemAPI.exceptions.appointment.InvalidAppointmentTimeException;
import ifba.edu.br.medSystemAPI.exceptions.appointment.PatientAlreadyHasAppointmentException;
import ifba.edu.br.medSystemAPI.exceptions.patient.PatientNotFoundException;
import ifba.edu.br.medSystemAPI.models.entities.Appointment;
import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import ifba.edu.br.medSystemAPI.models.entities.Patient;
import ifba.edu.br.medSystemAPI.models.enums.AppointmentStatus;
import ifba.edu.br.medSystemAPI.repositories.AppointmentRepository;
import ifba.edu.br.medSystemAPI.repositories.DoctorRepository;
import ifba.edu.br.medSystemAPI.repositories.PatientRepository;

@Service
public class AppointmentService {
  
  public PatientRepository patientRepository;
  public DoctorRepository doctorRepository;
  public AppointmentRepository appointmentRepository;

  public AppointmentService (PatientRepository patientRepository, DoctorRepository doctorRepository,AppointmentRepository appointmentRepository) {
    this.patientRepository = patientRepository;
    this.doctorRepository = doctorRepository;
    this.appointmentRepository = appointmentRepository;
  }

  // métodos auxiliares e/ou de validação

  private Doctor selectRandomDoctor(LocalDateTime appointmentTime) {
    List<Doctor> allDoctors = doctorRepository.findAll()
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


  private boolean isValidBusinessHour (LocalDateTime appointmentTime) {
    boolean isSunday = appointmentTime.getDayOfWeek() != DayOfWeek.SUNDAY;

    LocalTime start = LocalTime.of(7,0);
    LocalTime end = LocalTime.of(19,0);
    LocalTime time = appointmentTime.toLocalTime();
    boolean isBusinessHour = !time.isBefore(start) && time.isBefore(end);
    return isSunday && isBusinessHour;
  }
  
  private boolean hasMinimumAdvanceTime (LocalDateTime appointmentTime) {
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
    List<Appointment> doctorsScheduledAppointments = appointmentRepository.findByDoctorAndAppointmentTimeBetween(doctor, startTime, endTime)
    .stream()
    .filter(
      appointment -> appointment.getStatus() == AppointmentStatus.SCHEDULED
    ).toList();;

    return doctorsScheduledAppointments.isEmpty();
  }
  
  private boolean hasAppointmentSameDay(Patient patient, LocalDateTime appointmentTime) {
    LocalDateTime starDate = appointmentTime.toLocalDate().atStartOfDay();
    LocalDateTime endDate = appointmentTime.toLocalDate().atTime(23, 59, 59);
    List<Appointment> patientsScheduledAppointments = appointmentRepository.findByPatientAndAppointmentTimeBetween(patient, starDate, endDate)
    .stream()
    .filter(
      appointment -> appointment.getStatus() == AppointmentStatus.SCHEDULED
    ).toList();
    
    return !patientsScheduledAppointments.isEmpty();
  }

  private boolean canBeCancelled(Appointment appointment) {
    boolean isScheduled = appointment.getStatus() == AppointmentStatus.SCHEDULED;
    boolean hasMinimum24Hours = Duration.between(LocalDateTime.now(), appointment.getAppointmentTime()).toHours() >= 24;
    return isScheduled && hasMinimum24Hours;
  }

  private void validateSchedulingRules(Patient patient, Doctor doctor, LocalDateTime appointmentTime) {

    if (!isValidBusinessHour(appointmentTime)) {
      throw new InvalidAppointmentTimeException("Appointments must be scheduled Monday to Saturday, between 07:00 and 19:00");
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
      throw new DoctorNotAvailableException(doctor.getId(), appointmentTime);
    }

    if (!isDoctorAvailableForOneHour(doctor, appointmentTime)) {
      throw new DoctorNotAvailableException("Doctor not available for the full hour duration");
    }

    if (hasAppointmentSameDay(patient, appointmentTime)) {
      throw new PatientAlreadyHasAppointmentException(patient.getId(), appointmentTime.toLocalDate());
    }

  }


  private void validateCancellationRules(Appointment appointment) {
    if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
      throw new AppointmentCannotBeCancelledException(appointment.getId(), "Only scheduled appointments can be cancelled");
    }

    if (!canBeCancelled(appointment)) {
      throw new AppointmentCannotBeCancelledException(appointment.getId(), "Must be cancelled at least 24 hours in advance");
    }

  }

  // métodos get, create e delete
  
  public AppointmentDTO getAppointmentById (Long id) {
    return new AppointmentDTO(this.appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id)));
  }

  public List<AppointmentDTO> getAllAppointment () {
    return this.appointmentRepository.findAll().stream().map(AppointmentDTO::new).toList();
  }
  
  public AppointmentDTO scheduleAppointment (AppointmentCreateDTO appointment) {
    Patient patientInfo = this.patientRepository.findById(appointment.patientId()).orElseThrow(() -> new PatientNotFoundException(appointment.patientId()));

    Doctor doctorInfo;
    if (appointment.doctorId() == null) {
      doctorInfo = selectRandomDoctor(appointment.appointmentTime());
    } else {
      doctorInfo = this.doctorRepository.findById(appointment.doctorId()).orElseThrow(() -> new DoctorNotAvailableException("Doctor not found with ID: " + appointment.doctorId()));
    }

    validateSchedulingRules(patientInfo, doctorInfo, appointment.appointmentTime());

    Appointment newAppointment = new Appointment(patientInfo, doctorInfo, appointment.appointmentTime(), LocalDateTime.now());

    return new AppointmentDTO(this.appointmentRepository.save(newAppointment));
  }

  public AppointmentDTO cancelAppointment (Long id, AppointmentCancelDTO appointment) {
    Appointment existingAppointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));

    validateCancellationRules(existingAppointment);

    existingAppointment.setStatus(AppointmentStatus.CANCELLED);
    existingAppointment.setCancelledReason(appointment.cancelledReason());
    existingAppointment.setCancelledTime(LocalDateTime.now());

    return new AppointmentDTO(this.appointmentRepository.save(existingAppointment));

  }

}