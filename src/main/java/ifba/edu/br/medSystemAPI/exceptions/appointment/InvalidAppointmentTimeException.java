package ifba.edu.br.medSystemAPI.exceptions.appointment;

public class InvalidAppointmentTimeException extends RuntimeException {
  public InvalidAppointmentTimeException(String message) {
    super("Invalid appointment time: " + message);
  }
  
  public InvalidAppointmentTimeException(String field, String value) {
    super("Invalid appointment " + field + ": " + value);
  }
}