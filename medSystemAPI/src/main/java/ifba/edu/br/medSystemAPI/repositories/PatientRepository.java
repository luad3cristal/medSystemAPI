package ifba.edu.br.medSystemAPI.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ifba.edu.br.medSystemAPI.models.entities.Patient;

public interface PatientRepository extends JpaRepository <Patient, Long> {
    Page<Patient> findByStatus(Pageable pageable, Boolean status);

    Page<Patient> findByStatusAndUserEnabled(Pageable pageable, Boolean status, Boolean enabled);

    Page<Patient> findByUserEnabledTrue(Pageable pageable);
}