package ifba.edu.br.medSystemAPI.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ifba.edu.br.medSystemAPI.models.entities.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findByStatus(Pageable pageable, Boolean status);

    Page<Doctor> findByStatusAndUserEnabled(Pageable pageable, Boolean status, Boolean enabled);

    Page<Doctor> findByUserEnabledTrue(Pageable pageable);

    List<Doctor> findByUserEnabledTrue();

    @Query("SELECT d from Doctor d WHERE d.id NOT IN " +
            "(SELECT a.doctor.id FROM Appointment a " +
            "WHERE a.appointmentTime = :appointmentTime AND a.status = ifba.edu.br.medSystemAPI.models.enums.AppointmentStatus.SCHEDULED)")
    List<Doctor> findAvailableDoctorsAtAppointmentTime(@Param("appointmentTime") LocalDateTime appointmentTime);

    // Buscar médico por CRM
    Optional<Doctor> findByCrm(String crm);

    Optional<Doctor> findByCrmAndUserEnabledTrue(String crm);

    // Buscar médicos por nome (case-insensitive)
    List<Doctor> findByNameContainingIgnoreCase(String name);

    List<Doctor> findByNameContainingIgnoreCaseAndUserEnabledTrue(String name);
}