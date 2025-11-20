package ifba.edu.br.medSystemAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ifba.edu.br.medSystemAPI.models.entities.Patient;

public interface PatientRepository extends JpaRepository <Patient, Long> {
    
}