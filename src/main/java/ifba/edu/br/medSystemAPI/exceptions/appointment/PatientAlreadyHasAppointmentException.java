package ifba.edu.br.medSystemAPI.exceptions.appointment;

import java.time.LocalDate;

public class PatientAlreadyHasAppointmentException extends RuntimeException {
  public PatientAlreadyHasAppointmentException(Long patientId, LocalDate date) {
    //TODO: passar o nome ao invés do id
    super("Patient with id " + patientId + " already has an appointment on " + date);
  }
  
  public PatientAlreadyHasAppointmentException(String message) {
    super("Patient already has appointment: " + message);
  }
}