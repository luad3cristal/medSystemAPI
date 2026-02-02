package ifba.edu.br.medSystemAPI.shared.exceptions.appointment;

import java.time.LocalDateTime;

import ifba.edu.br.medSystemAPI.models.entities.Doctor;

public class DoctorNotAvailableException extends RuntimeException {
  public DoctorNotAvailableException(Doctor doctor, LocalDateTime appointmentTime) {
    super("Doctor " + doctor.getName() + " (CRM: " + doctor.getCRM() + ") is not available at " + appointmentTime);
}
  
  public DoctorNotAvailableException(String message) {
    super("Doctor not available: " + message);
  }
}