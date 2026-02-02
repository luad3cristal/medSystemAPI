package ifba.edu.br.medSystemAPI.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.JWTVerificationException;

import ifba.edu.br.medSystemAPI.exceptions.appointment.AppointmentCannotBeCancelledException;
import ifba.edu.br.medSystemAPI.exceptions.appointment.AppointmentNotFoundException;
import ifba.edu.br.medSystemAPI.exceptions.appointment.DoctorNotAvailableException;
import ifba.edu.br.medSystemAPI.exceptions.appointment.InvalidAppointmentTimeException;
import ifba.edu.br.medSystemAPI.exceptions.appointment.PatientAlreadyHasAppointmentException;
import ifba.edu.br.medSystemAPI.exceptions.doctor.DoctorNotFoundException;
import ifba.edu.br.medSystemAPI.exceptions.doctor.DuplicateDoctorException;
import ifba.edu.br.medSystemAPI.exceptions.patient.DuplicatePatientException;
import ifba.edu.br.medSystemAPI.exceptions.patient.PatientNotFoundException;
import jakarta.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
  // doctor exception
  @ExceptionHandler(DoctorNotFoundException.class)
  public ResponseEntity<String> handleDoctorNotFound(DoctorNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(DuplicateDoctorException.class)
  public ResponseEntity<String> handleDuplicateDoctor(DuplicateDoctorException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handleDataIntegrity(DataIntegrityViolationException ex) {
    String message = "Data integrity violation: " + ex.getMessage();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
    String message = "Validation failed: " + ex.getMessage();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
  }

  // patient exceptions
  @ExceptionHandler(PatientNotFoundException.class)
  public ResponseEntity<String> handlePatientNotFound(PatientNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(DuplicatePatientException.class)
  public ResponseEntity<String> handleDuplicatePatient(DuplicatePatientException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  // appointment exceptions
  @ExceptionHandler(AppointmentNotFoundException.class)
  public ResponseEntity<String> handleAppointmentNotFound(AppointmentNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(InvalidAppointmentTimeException.class)
  public ResponseEntity<String> handleInvalidAppointmentTime(InvalidAppointmentTimeException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(DoctorNotAvailableException.class)
  public ResponseEntity<String> handleDoctorNotAvailable(DoctorNotAvailableException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(PatientAlreadyHasAppointmentException.class)
  public ResponseEntity<String> handlePatientAlreadyHasAppointment(PatientAlreadyHasAppointmentException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(AppointmentCannotBeCancelledException.class)
  public ResponseEntity<String> handleAppointmentCannotBeCancelled(AppointmentCannotBeCancelledException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  // authentication and security exceptions
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos");
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<String> handleDisabled(DisabledException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário aguardando aprovação do administrador");
  }

  @ExceptionHandler(JWTVerificationException.class)
  public ResponseEntity<String> handleJWTVerification(JWTVerificationException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou expirado");
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");
  }

  @ExceptionHandler(ifba.edu.br.medSystemAPI.exceptions.AccessDeniedException.class)
  public ResponseEntity<String> handleCustomAccessDenied(ifba.edu.br.medSystemAPI.exceptions.AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }
}
