package ifba.edu.br.medSystemAPI.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ifba.edu.br.medSystemAPI.exceptions.doctor.DoctorNotFoundException;
import ifba.edu.br.medSystemAPI.exceptions.doctor.DuplicateDoctorException;
import ifba.edu.br.medSystemAPI.exceptions.patient.DuplicatePatientException;
import ifba.edu.br.medSystemAPI.exceptions.patient.PatientNotFoundException;
import jakarta.validation.ConstraintViolationException;

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
}
