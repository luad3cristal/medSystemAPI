package ifba.edu.br.medSystemAPI.exceptions.appointment;

import java.time.LocalDateTime;

public class DoctorNotAvailableException extends RuntimeException {
  public DoctorNotAvailableException(Long doctorId, LocalDateTime appointmentTime) {
    //TODO: mostrar o nome do médico ao invés de o id
    super("Doctor with id " + doctorId + " is not available at " + appointmentTime);
  }
  
  public DoctorNotAvailableException(String message) {
    super("Doctor not available: " + message);
  }
}