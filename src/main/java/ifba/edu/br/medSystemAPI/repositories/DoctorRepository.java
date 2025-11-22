package ifba.edu.br.medSystemAPI.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ifba.edu.br.medSystemAPI.models.entities.Doctor;

public interface DoctorRepository extends JpaRepository <Doctor, Long> {
    @Query("SELECT d from Doctor d WHERE d.status = :status")
    Page<Doctor> findByStatus(Pageable pageable, @Param("status") Boolean status);
}