package ifba.edu.br.medSystemAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository <Patient, Long> {
    
}