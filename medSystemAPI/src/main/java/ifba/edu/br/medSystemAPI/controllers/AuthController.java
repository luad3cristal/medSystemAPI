package ifba.edu.br.medSystemAPI.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifba.edu.br.medSystemAPI.dtos.auth.request.DoctorRegisterDTO;
import ifba.edu.br.medSystemAPI.dtos.auth.request.LoginRequestDTO;
import ifba.edu.br.medSystemAPI.dtos.auth.request.PatientRegisterDTO;
import ifba.edu.br.medSystemAPI.dtos.auth.response.LoggedUserDTO;
import ifba.edu.br.medSystemAPI.dtos.auth.response.LoginResponseDTO;
import ifba.edu.br.medSystemAPI.models.entities.User;
import ifba.edu.br.medSystemAPI.models.enums.Specialty;
import ifba.edu.br.medSystemAPI.services.JWTokenService;
import ifba.edu.br.medSystemAPI.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationManager manager;
  private final UserService userService;
  private final JWTokenService tokenService;

  public AuthController(AuthenticationManager manager, UserService userService, JWTokenService tokenService) {
    this.manager = manager;
    this.userService = userService;
    this.tokenService = tokenService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dados) {
    var authToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.password());
    var authentication = manager.authenticate(authToken);

    var user = (User) authentication.getPrincipal();

    if (!user.isEnabled()) {
      return ResponseEntity.status(403).build();
    }

    var tokenJWT = tokenService.gerarToken(user);

    return ResponseEntity.ok(new LoginResponseDTO(
        tokenJWT,
        user.getRole().name(),
        user.getName(),
        user.getId()));
  }

  @GetMapping("/me")
  public ResponseEntity<LoggedUserDTO> me() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = (User) authentication.getPrincipal();
    return ResponseEntity.ok(new LoggedUserDTO(user));
  }

  @PostMapping("/register/medico")
  public ResponseEntity<String> registerDoctor(@RequestBody @Valid DoctorRegisterDTO dados) {
    userService.createDoctorUser(dados);
    return ResponseEntity.status(201).body("Cadastro realizado. Aguardando aprovação do administrador.");
  }

  @PostMapping("/register/paciente")
  public ResponseEntity<String> registerPatient(@RequestBody @Valid PatientRegisterDTO dados) {
    userService.createPatientUser(dados);
    return ResponseEntity.status(201).body("Cadastro realizado. Aguardando aprovação do administrador.");
  }

  @GetMapping("/specialties")
  public ResponseEntity<List<String>> getSpecialties() {
    List<String> specialties = Arrays.stream(Specialty.values())
        .map(Enum::name)
        .collect(Collectors.toList());
    return ResponseEntity.ok(specialties);
  }
}
