package ifba.edu.br.medSystemAPI.exceptions.patient;

public class DuplicatePatientException extends RuntimeException {
  public DuplicatePatientException(String field, String value) {
    super("Patient already exists with " + field + ": " + value);
  }
}
