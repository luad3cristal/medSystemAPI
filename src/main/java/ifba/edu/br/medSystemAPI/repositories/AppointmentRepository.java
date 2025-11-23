package ifba.edu.br.medSystemAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ifba.edu.br.medSystemAPI.models.entities.Appointment;

public interface AppointmentRepository extends JpaRepository <Appointment, Long> {
  
}
