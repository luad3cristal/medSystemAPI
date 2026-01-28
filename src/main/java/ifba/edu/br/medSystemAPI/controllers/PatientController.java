package ifba.edu.br.medSystemAPI.controllers;

import org.springframework.data.domain.Page;
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
  public Page<PatientDTO> getActivePatients (@PageableDefault(size = 10, sort = "name") Pageable pageable) {
    return patientService.listPatientsByStatus(pageable, true);
  } 

  @GetMapping("/all")
  public Page<PatientDTO> getAllPatients (@PageableDefault(size = 10, sort = "name") Pageable pageable) {
    return patientService.listAllPatients(pageable);
  } 

  @PostMapping
  public PatientDTO createPatient(@RequestBody @Valid PatientCreateDTO patient) {
    return patientService.createPatient(patient);
  }

  @PutMapping("/{id}") 
  public PatientDTO updatePatient(@PathVariable Long id, @RequestBody @Valid PatientUpdateDTO patient) {
    return patientService.updatePatient(id, patient);
  }

  @DeleteMapping("/{id}") 
  public PatientDTO deactivePatientAccount(@PathVariable Long id) {
    return patientService.deactivePatientAccount(id);
  }

}
