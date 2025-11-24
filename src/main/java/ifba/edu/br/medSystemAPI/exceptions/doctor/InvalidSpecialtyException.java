package ifba.edu.br.medSystemAPI.exceptions.doctor;

public class InvalidSpecialtyException extends RuntimeException {
  public InvalidSpecialtyException(String specialty) {
      super("Invalid specialty: " + specialty);
  }
}
