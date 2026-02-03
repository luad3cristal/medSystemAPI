package ifba.edu.br.medSystemAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ifba.edu.br.medSystemAPI.models.entities.Address;

public interface AddressRepository extends JpaRepository <Address, Long> {
    
}