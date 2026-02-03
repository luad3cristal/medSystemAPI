package ifba.edu.br.medSystemAPI.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.dtos.patient.request.PatientCreateDTO;
import ifba.edu.br.medSystemAPI.dtos.patient.request.PatientUpdateDTO;
import ifba.edu.br.medSystemAPI.dtos.patient.response.PatientDTO;
import ifba.edu.br.medSystemAPI.models.entities.Address;
import ifba.edu.br.medSystemAPI.models.entities.Patient;
import ifba.edu.br.medSystemAPI.repositories.PatientRepository;
import ifba.edu.br.medSystemAPI.shared.exceptions.patient.DuplicatePatientException;
import ifba.edu.br.medSystemAPI.shared.exceptions.patient.PatientNotFoundException;
import jakarta.validation.ConstraintViolationException;

@Service
public class PatientService {
  private PatientRepository patientRepository;
  private AddressService addressService;


  public PatientService (PatientRepository patientRepository, AddressService addressService) {
    this.patientRepository = patientRepository;
    this.addressService = addressService;
  }
  
  public PatientDTO createPatient (PatientCreateDTO patient) {
    try {
      Address patientAddress = new Address(patient.address());
      Patient newPatient = new Patient(patient.name(), patient.email(), patient.phone(), patient.cpf(), patientAddress);

      Patient savedPatient = this.patientRepository.save(newPatient);

      return new PatientDTO(savedPatient);
    } 
    catch (DataIntegrityViolationException exception) {
      String message = exception.getMessage().toLowerCase();
      
      if(message.contains("cpf")) {
        throw new DuplicatePatientException("CPF", patient.cpf());
      }
      else if (message.contains("email")) {
        throw new DuplicatePatientException("email", patient.email());
      }

      throw exception;   
    } 
    catch (ConstraintViolationException exception) {
      throw exception;
    }    
  }

  public Page<PatientDTO> listPatientsByStatus(Pageable pageable, Boolean status) {
    return this.patientRepository.findByStatusAndUserEnabled(pageable, status, true).map(PatientDTO::new);
  }

  public Page<PatientDTO> listAllPatients(Pageable pageable) {
    return this.patientRepository.findByUserEnabledTrue(pageable).map(PatientDTO::new);
  }

  public void updatePatientAddress (Patient patient, AddressRequestDTO address) {
    Address existingAddress = patient.getAddress();

    if (existingAddress != null) {
      addressService.updateAddress(existingAddress, address);
    }
    else {
      patient.setAddress(addressService.createAddress(address));
    }
  }

  public PatientDTO updatePatient(Long id, PatientUpdateDTO patient) {
    Patient existingPatient = this.patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));

    existingPatient.setName(patient.name());
    existingPatient.setPhone(patient.phone());
    existingPatient.setStatus(patient.status());
    updatePatientAddress(existingPatient, patient.address());

    return new PatientDTO(this.patientRepository.save(existingPatient));
  }

  public PatientDTO deactivePatientAccount (Long id) {
    Patient patient = this.patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
  
    patient.setStatus(false);
    this.patientRepository.save(patient);

    return new PatientDTO(patient);
  }
}
