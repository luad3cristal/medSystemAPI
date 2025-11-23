package ifba.edu.br.medSystemAPI.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ifba.edu.br.medSystemAPI.repositories.DoctorRepository;
import ifba.edu.br.medSystemAPI.dtos.address.request.AddressRequestDTO;
import ifba.edu.br.medSystemAPI.dtos.doctor.request.DoctorCreateDTO;
import ifba.edu.br.medSystemAPI.dtos.doctor.request.DoctorUpdateDTO;
import ifba.edu.br.medSystemAPI.dtos.doctor.response.DoctorDTO;
import ifba.edu.br.medSystemAPI.models.entities.Address;
import ifba.edu.br.medSystemAPI.models.entities.Doctor;
import ifba.edu.br.medSystemAPI.models.enums.Specialty;

@Service
public class DoctorService {
  private DoctorRepository doctorRepository;
  private AddressService addressService;

  public DoctorService (DoctorRepository doctorRepository, AddressService addressService) {
    this.doctorRepository = doctorRepository;
    this.addressService = addressService;
  }

  public DoctorDTO createDoctor (DoctorCreateDTO doctor) {
    // TODO: Tratar DataIntegrityViolationException para CRM/Email duplicado
    // TODO: Tratar IllegalArgumentException para specialty inválida (do construtor Doctor)
    // TODO: Tratar ConstraintViolationException para validações JPA
    Specialty doctorSpecialty = Specialty.valueOf(doctor.specialty().toUpperCase().trim());
    Address doctorAddress = new Address(doctor.address());
    Doctor newDoctor = new Doctor.Builder().name(doctor.name()).email(doctor.email()).phone(doctor.phone()).crm(doctor.crm()).specialty(doctorSpecialty).address(doctorAddress).build();
    
    return new DoctorDTO(this.doctorRepository.save(newDoctor));
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
    // TODO: Tratar EntityNotFoundException quando médico não for encontrado
    // TODO: Validar se médico está ativo antes de atualizar
    Doctor existingDoctor = this.doctorRepository.findById(id).orElse(null);

    if (existingDoctor != null) {
      existingDoctor.setName(doctor.name());
      existingDoctor.setPhone(doctor.phone());
      updateDoctorAddress(existingDoctor, doctor.address());

      return new DoctorDTO(this.doctorRepository.save(existingDoctor));
    }
    // TODO: Melhor usar orElseThrow() em vez de retornar null
    return null;
  }

  public DoctorDTO deactiveDoctorAccount (Long id) {
    // TODO: Tratar EntityNotFoundException quando médico não for encontrado
    // TODO: Validar se médico pode ser desativado (ex: não tem consultas agendadas)
    Optional<Doctor> doctorOp = this.doctorRepository.findById(id);
    if (doctorOp.isPresent()) {
      Doctor doctor = doctorOp.get();
      doctor.setStatus(false);
      this.doctorRepository.save(doctor);

      return new DoctorDTO(doctor);
    }
    
    // TODO: Melhor usar orElseThrow() em vez de retornar null
    return null;
  }

}
