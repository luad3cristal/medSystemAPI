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

import ifba.edu.br.medSystemAPI.dtos.doctor.request.DoctorCreateDTO;
import ifba.edu.br.medSystemAPI.dtos.doctor.request.DoctorUpdateDTO;
import ifba.edu.br.medSystemAPI.dtos.doctor.response.DoctorDTO;
import ifba.edu.br.medSystemAPI.services.DoctorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
  private DoctorService doctorService;

  public DoctorController (DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  @GetMapping
  public Page<DoctorDTO> getActiveDoctors (@PageableDefault(size = 10, sort = "name") Pageable pageable) {
    return doctorService.listActiveDoctors(pageable);
  } 

  @GetMapping("/all")
  public Page<DoctorDTO> getAllDoctors (@PageableDefault(size = 10, sort = "name") Pageable pageable) {
    return doctorService.listAllDoctors(pageable);
  } 

  @PostMapping
  public DoctorDTO createDoctor(@RequestBody @Valid DoctorCreateDTO doctor) {
    return doctorService.createDoctor(doctor);
  }

  @PutMapping("/{id}") 
  public DoctorDTO updateDoctor(@PathVariable Long id, @RequestBody @Valid DoctorUpdateDTO doctor) {
    return doctorService.updateDoctor(id, doctor);
  }

  @DeleteMapping("/{id}") 
  public DoctorDTO deactiveDoctorAccount(@PathVariable Long id) {
    return doctorService.deactiveDoctorAccount(id);
  }

}
