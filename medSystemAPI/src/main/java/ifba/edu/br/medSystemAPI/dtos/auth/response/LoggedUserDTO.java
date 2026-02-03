package ifba.edu.br.medSystemAPI.dtos.auth.response;

import ifba.edu.br.medSystemAPI.dtos.doctor.response.DoctorDTO;
import ifba.edu.br.medSystemAPI.dtos.patient.response.PatientDTO;
import ifba.edu.br.medSystemAPI.models.entities.User;

public record LoggedUserDTO(
    Long userId,
    String role,
    String name,
    String email,
    PatientDTO patient,
    DoctorDTO doctor) {

  public LoggedUserDTO(User user) {
    this(
        user.getId(),
        user.getRole().name(),
        user.getName(),
        user.getEmail(),
        user.getPatient() == null ? null : new PatientDTO(user.getPatient()),
        user.getDoctor() == null ? null : new DoctorDTO(user.getDoctor()));
  }
}