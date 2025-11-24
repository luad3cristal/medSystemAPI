package ifba.edu.br.medSystemAPI.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.dtos.doctor.request.DoctorCreateDTO;
import ifba.edu.br.medSystemAPI.dtos.doctor.request.DoctorUpdateDTO;
import ifba.edu.br.medSystemAPI.dtos.doctor.response.DoctorDTO;
import ifba.edu.br.medSystemAPI.exceptions.doctor.DoctorNotFoundException;
import ifba.edu.br.medSystemAPI.exceptions.doctor.DuplicateDoctorException;
import ifba.edu.br.medSystemAPI.exceptions.doctor.InvalidSpecialtyException;
import ifba.edu.br.medSystemAPI.models.entities.Address;
import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import ifba.edu.br.medSystemAPI.models.enums.Specialty;
import ifba.edu.br.medSystemAPI.repositories.DoctorRepository;
import jakarta.validation.ConstraintViolationException;

@Service
public class DoctorService {
  private DoctorRepository doctorRepository;
  private AddressService addressService;

  public DoctorService (DoctorRepository doctorRepository, AddressService addressService) {
    this.doctorRepository = doctorRepository;
    this.addressService = addressService;
  }

  public DoctorDTO createDoctor (DoctorCreateDTO doctor) {
    try {
      Specialty doctorSpecialty;
      try {
        doctorSpecialty =  Specialty.valueOf(doctor.specialty().toUpperCase().trim());
      } catch (IllegalArgumentException exception) {
        throw new InvalidSpecialtyException(doctor.specialty());
      }

      Address doctorAddress = new Address(doctor.address());

      Doctor newDoctor = new Doctor(doctor.name(), doctor.email(), doctor.phone(), doctor.crm(), doctorSpecialty, doctorAddress);
      
      Doctor savedDoctor = doctorRepository.save(newDoctor);

      return new DoctorDTO(savedDoctor);
    } catch (DataIntegrityViolationException exception) {
      String message = exception.getMessage().toLowerCase();
      
      if(message.contains("crm")) {
        throw new DuplicateDoctorException("CRM", doctor.crm());
      }
      else if (message.contains("email")) {
        throw new DuplicateDoctorException("email", doctor.email());
      }
      throw exception;
    
    } catch (ConstraintViolationException exception) {
      throw exception;
    }
  }

  public Page<DoctorDTO> listDoctorsByStatus(Pageable pageable, Boolean status) {
    return this.doctorRepository.findByStatus(pageable, status).map(DoctorDTO::new);
  }

  public Page<DoctorDTO> listAllDoctors(Pageable pageable) {
    return this.doctorRepository.findAll(pageable).map(DoctorDTO::new);
  }

  public void updateDoctorAddress (Doctor doctor, AddressRequestDTO address) {
    Address existingAddress = doctor.getAddress();

    if (existingAddress != null) {
      addressService.updateAddress(existingAddress, address);
    }
    else {
      doctor.setAddress(addressService.createAddress(address));
    }
  }

  public DoctorDTO updateDoctor(Long id, DoctorUpdateDTO doctor) {
    Doctor existingDoctor = this.doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));

    if (existingDoctor.getStatus()) {
      existingDoctor.setName(doctor.name());
      existingDoctor.setPhone(doctor.phone());
      updateDoctorAddress(existingDoctor, doctor.address());

      return new DoctorDTO(this.doctorRepository.save(existingDoctor));
    }
    
    return null;
  }

  public DoctorDTO deactiveDoctorAccount (Long id) {
    Doctor doctor = this.doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
    doctor.setStatus(false);
    this.doctorRepository.save(doctor);

    return new DoctorDTO(doctor);
  }

}
