package ifba.edu.br.medSystemAPI.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ifba.edu.br.medSystemAPI.models.entities.Patient;

public interface PatientRepository extends JpaRepository <Patient, Long> {
    // TODO: Considerar usar método de convenção: findByStatus(Boolean status, Pageable pageable)
    @Query("SELECT p from Patient p WHERE p.status = :status")
    Page<Patient> findByStatus(Pageable pageable, @Param("status") Boolean status);
}