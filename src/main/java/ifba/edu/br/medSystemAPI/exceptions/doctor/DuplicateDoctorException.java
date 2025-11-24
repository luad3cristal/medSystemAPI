package ifba.edu.br.medSystemAPI.exceptions.doctor;

public class DuplicateDoctorException extends RuntimeException {
  public DuplicateDoctorException(String field, String value) {
      super("Doctor already exists with " + field + ": " + value);
  }
}
