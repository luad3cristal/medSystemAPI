package ifba.edu.br.medSystemAPI.shared.exceptions.patient;

public class PatientNotFoundException extends RuntimeException {
  public PatientNotFoundException(Long id) {
    super("Patient not found with id: " + id);
  }
}
