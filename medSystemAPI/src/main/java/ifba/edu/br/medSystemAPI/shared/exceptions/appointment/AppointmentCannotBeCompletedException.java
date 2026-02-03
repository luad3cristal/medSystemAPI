package ifba.edu.br.medSystemAPI.shared.exceptions.appointment;

public class AppointmentCannotBeCompletedException extends RuntimeException {
  public AppointmentCannotBeCompletedException(Long id, String message) {
    super("Appointment " + id + " cannot be completed: " + message);
  }
}