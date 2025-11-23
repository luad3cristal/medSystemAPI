package ifba.edu.br.medSystemAPI.services;

import java.util.Optional;

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

@Service
public class PatientService {
  private PatientRepository patientRepository;
  private AddressService addressService;


  public PatientService (PatientRepository patientRepository, AddressService addressService) {
    this.patientRepository = patientRepository;
    this.addressService = addressService;
  }
  
  public PatientDTO createPatient (PatientCreateDTO patient) {
    // TODO: Tratar DataIntegrityViolationException para CPF/Email duplicado
    // TODO: Tratar ConstraintViolationException para validações JPA
    Address patientAddress = new Address(patient.address());
    Patient newPatient = new Patient(patient.name(), patient.email(), patient.phone(), patient.cpf(), patientAddress);

    return new PatientDTO(this.patientRepository.save(newPatient));
  }

  public Page<PatientDTO> listPatientsByStatus(Pageable pageable, Boolean status) {
    return this.patientRepository.findByStatus(pageable, status).map(PatientDTO::new);
  }

  public Page<PatientDTO> listAllPatients(Pageable pageable) {
    return this.patientRepository.findAll(pageable).map(PatientDTO::new);
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
    // TODO: Tratar EntityNotFoundException quando paciente não for encontrado
    // TODO: Validar se paciente está ativo antes de atualizar
    Patient existingPatient = this.patientRepository.findById(id).orElse(null);

    if (existingPatient != null) {
      existingPatient.setName(patient.name());
      existingPatient.setPhone(patient.phone());
      updatePatientAddress(existingPatient, patient.address());

      return new PatientDTO(this.patientRepository.save(existingPatient));
    }
    // TODO: Melhor usar orElseThrow() em vez de retornar null
    return null;
  }

  public PatientDTO deactivePatientAccount (Long id) {
    // TODO: Tratar EntityNotFoundException quando paciente não for encontrado
    // TODO: Validar se paciente pode ser desativado (ex: não tem consultas agendadas)
    Optional<Patient> patientOp = this.patientRepository.findById(id);
    if (patientOp.isPresent()) {
      Patient patient = patientOp.get();
      patient.setStatus(false);
      this.patientRepository.save(patient);

      return new PatientDTO(patient);
    }
    
    // TODO: Melhor usar orElseThrow() em vez de retornar null
    return null;
  }
}
