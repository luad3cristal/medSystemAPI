package ifba.edu.br.medSystemAPI.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ifba.edu.br.medSystemAPI.models.entities.Doctor;

public interface DoctorRepository extends JpaRepository <Doctor, Long> {
    Page<Doctor> findAllActive(Pageable pageable, Boolean status);
}