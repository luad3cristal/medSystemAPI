package ifba.edu.br.medSystemAPI.models.entities;

import java.time.LocalDateTime;

import ifba.edu.br.medSystemAPI.models.enums.AppointmentStatus;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointments")
public class Appointment {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Patient patient;
  @ManyToOne
  private Doctor doctor;

  private LocalDateTime appointmentTime;
  private AppointmentStatus status = AppointmentStatus.SCHEDULED;
  
  private LocalDateTime createdAt;
  
  @Nullable
  private LocalDateTime cancelledAt;

  @Nullable
  private String cancelledReason;

  public Appointment () {

  }

  public Appointment (Patient patient, Doctor doctor, LocalDateTime appointmentTime,LocalDateTime createdAt) {
    this.patient = patient;
    this.doctor = doctor;
    this.appointmentTime = appointmentTime;
    this.createdAt = createdAt;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Patient getPatient() { return patient; }
  public void setPatient(Patient patient) { this.patient = patient; }

  public Doctor getDoctor() { return doctor; }
  public void setDoctor(Doctor doctor) { this.doctor = doctor; }

  public LocalDateTime getAppointmentTime() { return appointmentTime; }
  public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }

  public AppointmentStatus getStatus() { return status; }
  public void setStatus(AppointmentStatus status) { this.status = status; }

  public LocalDateTime getCreatedTime() { return createdAt; }
  public void setCreatedTime(LocalDateTime createdAt) { this.createdAt = createdAt; }

  public LocalDateTime getCancelledTime() { return cancelledAt; }
  public void setCancelledTime(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }

  public String getCancelledReason() { return cancelledReason; }
  public void setCancelledReason(String cancelledReason) { this.cancelledReason = cancelledReason; }
}
