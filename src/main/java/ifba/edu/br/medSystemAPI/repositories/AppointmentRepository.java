package ifba.edu.br.medSystemAPI.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ifba.edu.br.medSystemAPI.models.entities.Appointment;
import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import ifba.edu.br.medSystemAPI.models.entities.Patient;
import ifba.edu.br.medSystemAPI.models.enums.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
  List<Appointment> findByPatientAndAppointmentTime(Patient patient, LocalDateTime time);

  List<Appointment> findByDoctorAndAppointmentTime(Doctor doctor, LocalDateTime time);

  List<Appointment> findByStatusAndAppointmentTime(AppointmentStatus status, LocalDateTime time);

  List<Appointment> findByDoctorAndAppointmentTimeBetween(Doctor doctor, LocalDateTime start, LocalDateTime end);

  List<Appointment> findByPatientAndAppointmentTimeBetween(Patient patient, LocalDateTime start, LocalDateTime end);

  List<Appointment> findByDoctorAndAppointmentTimeAfter(Doctor doctor, LocalDateTime time);

  boolean existsByDoctorAndAppointmentTime(Doctor doctor, LocalDateTime time);

  Page<Appointment> findByPatient(Patient patient, Pageable pageable);

  Page<Appointment> findByDoctor(Doctor doctor, Pageable pageable);
}
