package ifba.edu.br.medSystemAPI.controllers;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifba.edu.br.medSystemAPI.dtos.patient.request.PatientCreateDTO;
import ifba.edu.br.medSystemAPI.dtos.patient.request.PatientUpdateDTO;
import ifba.edu.br.medSystemAPI.dtos.patient.response.PatientDTO;
import ifba.edu.br.medSystemAPI.services.PatientService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/patients")
public class PatientController {
  private PatientService patientService;

  public PatientController (PatientService patientService) {
    this.patientService = patientService;
  }

  @GetMapping
  public List<PatientDTO> getActivePatients (@PageableDefault(size = 10, sort = "name") Pageable pageable) {
    // TODO: Considerar retornar Page<PatientDTO> em vez de List para manter info de paginação
    return patientService.listPatientsByStatus(pageable, true).getContent();
  } 

  @GetMapping("/all")
  public List<PatientDTO> getAllPatients (@PageableDefault(size = 10, sort = "name") Pageable pageable) {
    // TODO: Considerar retornar Page<PatientDTO> em vez de List para manter info de paginação
    return patientService.listAllPatients(pageable).getContent();
  } 

  @PostMapping
  public PatientDTO createPatient(@RequestBody @Valid PatientCreateDTO patient) {
    // TODO: Tratar DataIntegrityViolationException para CPF/Email duplicado
    // TODO: Considerar usar ResponseEntity<PatientDTO> com status 201 CREATED
    return patientService.createPatient(patient);
  }

  @PutMapping("/{id}") 
  public PatientDTO updatePatient(@PathVariable Long id, @RequestBody @Valid PatientUpdateDTO patient) {
    // TODO: Tratar EntityNotFoundException quando paciente não for encontrado
    // TODO: Validar se ID é válido (não negativo/zero)
    return patientService.updatePatient(id, patient);
  }

  @DeleteMapping("/{id}") 
  public PatientDTO deactivePatientAccount(@PathVariable Long id) {
    // TODO: Tratar EntityNotFoundException quando paciente não for encontrado
    // TODO: Validar se ID é válido (não negativo/zero)
    // TODO: Considerar usar ResponseEntity<PatientDTO> com status 204 NO CONTENT
    return patientService.deactivePatientAccount(id);
  }

}
