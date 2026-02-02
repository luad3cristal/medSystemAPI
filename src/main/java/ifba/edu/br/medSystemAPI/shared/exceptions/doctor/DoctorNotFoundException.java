package ifba.edu.br.medSystemAPI.shared.exceptions.doctor;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(Long id) {
        super("Doctor not found with id: " + id);
    }
}
