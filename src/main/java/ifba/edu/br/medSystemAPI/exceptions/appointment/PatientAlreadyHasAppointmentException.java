package ifba.edu.br.medSystemAPI.exceptions.appointment;

import java.time.LocalDate;

import ifba.edu.br.medSystemAPI.models.entities.Patient;

public class PatientAlreadyHasAppointmentException extends RuntimeException {
  public PatientAlreadyHasAppointmentException(Patient patient, LocalDate date) {
    //TODO: passar o nome ao invés do id
    super("Patient " + patient.getName() + "(CPF: " + patient.getCPF() + ") already has an appointment on " + date);
  }
  
  public PatientAlreadyHasAppointmentException(String message) {
    super("Patient already has appointment: " + message);
  }
}