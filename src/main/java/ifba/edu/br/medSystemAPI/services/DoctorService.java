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
    var newDoctor = this.doctorRepository.save(new Doctor(doctor));
    return new DoctorDTO(newDoctor);
  }

  public Page<DoctorDTO> listActiveDoctors(Pageable pageable) {
    return this.doctorRepository.findAllActive(pageable, true).map(DoctorDTO::new);
  }

  public DoctorDTO updateDoctor(Long id, DoctorUpdateDTO doctor) {
    Doctor checkDoctor = this.doctorRepository.findById(id).orElse(null);

    if (checkDoctor != null) {
      checkDoctor.setName(doctor.name());
      checkDoctor.setPhone(doctor.phone());
      checkDoctor.setAddress(new Address(doctor.address()));
      this.doctorRepository.save(checkDoctor);
    }

    return new DoctorDTO(checkDoctor);
  }

  public void deactiveDoctorAccount (Long id) {
    Optional<Doctor> checkDoctor = this.doctorRepository.findById(id);
    if (checkDoctor.isPresent()) {
      this.doctorRepository.deleteById(id);
    }
  }

}
