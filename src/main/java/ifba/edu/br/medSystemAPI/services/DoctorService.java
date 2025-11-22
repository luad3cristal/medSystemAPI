package ifba.edu.br.medSystemAPI.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ifba.edu.br.medSystemAPI.repositories.DoctorRepository;
import ifba.edu.br.medSystemAPI.dtos.doctor.request.DoctorCreateDTO;
import ifba.edu.br.medSystemAPI.dtos.doctor.request.DoctorUpdateDTO;
import ifba.edu.br.medSystemAPI.dtos.doctor.response.DoctorDTO;
import ifba.edu.br.medSystemAPI.models.entities.Address;
import ifba.edu.br.medSystemAPI.models.entities.Doctor;

@Service
public class DoctorService {
  private DoctorRepository doctorRepository;

  public DoctorService (DoctorRepository doctorRepository) {
    this.doctorRepository = doctorRepository;
  }

  public DoctorDTO createDoctor (DoctorCreateDTO doctor) {
    // TODO: Tratar DataIntegrityViolationException para CRM/Email duplicado
    // TODO: Tratar IllegalArgumentException para specialty inválida (do construtor Doctor)
    // TODO: Tratar ConstraintViolationException para validações JPA
    var newDoctor = this.doctorRepository.save(new Doctor(doctor));
    return new DoctorDTO(newDoctor);
  }

  public Page<DoctorDTO> listActiveDoctors(Pageable pageable) {
    return this.doctorRepository.findAllActive(pageable, true).map(DoctorDTO::new);
  }

  public DoctorDTO updateDoctor(Long id, DoctorUpdateDTO doctor) {
    // TODO: Tratar EntityNotFoundException quando médico não for encontrado
    // TODO: Validar se médico está ativo antes de atualizar
    Doctor checkDoctor = this.doctorRepository.findById(id).orElse(null);

    if (checkDoctor != null) {
      checkDoctor.setName(doctor.name());
      checkDoctor.setPhone(doctor.phone());
      // TODO: Tratar NullPointerException no construtor Address
      checkDoctor.setAddress(new Address(doctor.address()));
      // TODO: Tratar ConstraintViolationException para validações JPA
      this.doctorRepository.save(checkDoctor);
    }

    // TODO: Tratar NullPointerException se checkDoctor for null
    return new DoctorDTO(checkDoctor);
  }

  public void deactiveDoctorAccount (Long id) {
    // TODO: Tratar EntityNotFoundException quando médico não for encontrado
    // TODO: Implementar soft delete (status = false) em vez de delete físico
    // TODO: Validar se médico pode ser desativado (ex: não tem consultas agendadas)
    Optional<Doctor> checkDoctor = this.doctorRepository.findById(id);
    if (checkDoctor.isPresent()) {
      // TODO: Tratar EmptyResultDataAccessException se ID não existir
      this.doctorRepository.deleteById(id);
    }
  }

}
