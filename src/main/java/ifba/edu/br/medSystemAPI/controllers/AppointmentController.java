package ifba.edu.br.medSystemAPI.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifba.edu.br.medSystemAPI.dtos.appointment.request.AppointmentCancelDTO;
import ifba.edu.br.medSystemAPI.dtos.appointment.request.AppointmentCreateDTO;
import ifba.edu.br.medSystemAPI.dtos.appointment.response.AppointmentDTO;
import ifba.edu.br.medSystemAPI.services.AppointmentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
  private AppointmentService appointmentService;

  public AppointmentController (AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  @GetMapping
  public ResponseEntity<Page<AppointmentDTO>> getAppointments(@PageableDefault(size = 10, sort = "appointmentTime") Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
    .body(appointmentService.getAllAppointments(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AppointmentDTO> getAppointmentById (@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK)
    .body(appointmentService.getAppointmentById(id));
  }

  @PostMapping()
  public ResponseEntity<AppointmentDTO> scheduleAppointment(@RequestBody @Valid AppointmentCreateDTO appointment) {
    return ResponseEntity.status(HttpStatus.CREATED)
    .body(appointmentService.scheduleAppointment(appointment));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable Long id, @RequestBody @Valid AppointmentCancelDTO appointment) {
    return ResponseEntity.status(HttpStatus.OK)
    .body(appointmentService.cancelAppointment(id, appointment));
  }
  
}
