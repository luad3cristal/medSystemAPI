package ifba.edu.br.medSystemAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository <Doctor, Long> {
    
}