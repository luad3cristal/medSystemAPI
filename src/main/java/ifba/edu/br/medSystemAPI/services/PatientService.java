package ifba.edu.br.medSystemAPI.services;

import ifba.edu.br.medSystemAPI.repositories.PatientRepository;

public class PatientService {
  private PatientRepository patientRepository;

  public PatientService (PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }
  
}
