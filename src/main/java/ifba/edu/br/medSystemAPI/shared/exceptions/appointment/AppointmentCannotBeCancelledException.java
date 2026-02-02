package ifba.edu.br.medSystemAPI.shared.exceptions.appointment;

public class AppointmentCannotBeCancelledException extends RuntimeException {
  public AppointmentCannotBeCancelledException(String reason) {
    super("Appointment cannot be cancelled: " + reason);
  }
  
  public AppointmentCannotBeCancelledException(Long appointmentId, String reason) {
    super("Appointment with id " + appointmentId + " cannot be cancelled: " + reason);
  }
}