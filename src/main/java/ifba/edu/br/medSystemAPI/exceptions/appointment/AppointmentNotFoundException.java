package ifba.edu.br.medSystemAPI.exceptions.appointment;

public class AppointmentNotFoundException extends RuntimeException {
  public AppointmentNotFoundException(Long id) {
    super("Appointment not found with id: " + id);
  }
}
