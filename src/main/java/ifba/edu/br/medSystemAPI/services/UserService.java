package ifba.edu.br.medSystemAPI.services;

import ifba.edu.br.medSystemAPI.dtos.auth.request.DoctorRegisterDTO;
import ifba.edu.br.medSystemAPI.dtos.auth.request.PatientRegisterDTO;
import ifba.edu.br.medSystemAPI.dtos.auth.response.PendingUserDTO;
import ifba.edu.br.medSystemAPI.models.entities.Address;
import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import ifba.edu.br.medSystemAPI.models.entities.Patient;
import ifba.edu.br.medSystemAPI.models.entities.User;
import ifba.edu.br.medSystemAPI.models.enums.Role;
import ifba.edu.br.medSystemAPI.models.enums.Specialty;
import ifba.edu.br.medSystemAPI.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }    @Transactional
    public User createDoctorUser(DoctorRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new ifba.edu.br.medSystemAPI.exceptions.EmailAlreadyExistsException("Email já cadastrado no sistema");
        }

    User user = new User();
    user.setEmail(dto.email());
    user.setPassword(passwordEncoder.encode(dto.password()));
    user.setRole(Role.DOCTOR);
    user.setEnabled(false);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());

    Doctor doctor = new Doctor();
    doctor.setName(dto.name());
    doctor.setEmail(dto.email());
    doctor.setPhone(dto.phone());
    doctor.setCRM(dto.crm());
    doctor.setSpecialty(Specialty.valueOf(dto.specialty()));

    Address address = new Address(
        dto.address().street(),
        dto.address().number(),
        dto.address().complement(),
        dto.address().neighborhood(),
        dto.address().city(),
        dto.address().state(),
        dto.address().zipCode());
    doctor.setAddress(address);
    doctor.setUser(user);

    user.setDoctor(doctor);

    return userRepository.save(user);
  }    @Transactional
    public User createPatientUser(PatientRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new ifba.edu.br.medSystemAPI.exceptions.EmailAlreadyExistsException("Email já cadastrado no sistema");
        }

    User user = new User();
    user.setEmail(dto.email());
    user.setPassword(passwordEncoder.encode(dto.password()));
    user.setRole(Role.PATIENT);
    user.setEnabled(false);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());

    Patient patient = new Patient();
    patient.setName(dto.name());
    patient.setEmail(dto.email());
    patient.setPhone(dto.phone());
    patient.setCPF(dto.cpf());

    Address address = new Address(
        dto.address().street(),
        dto.address().number(),
        dto.address().complement(),
        dto.address().neighborhood(),
        dto.address().city(),
        dto.address().state(),
        dto.address().zipCode());
    patient.setAddress(address);
    patient.setUser(user);

    user.setPatient(patient);

    return userRepository.save(user);
  }

  public List<PendingUserDTO> getPendingUsers() {
    return userRepository.findByEnabledFalse()
        .stream()
        .map(PendingUserDTO::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public void approveUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    user.setEnabled(true);
    user.setUpdatedAt(LocalDateTime.now());
    userRepository.save(user);
  }

  @Transactional
  public void rejectUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    userRepository.delete(user);
  }

  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }
}
