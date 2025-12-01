package ifba.edu.br.medSystemAPI.controllers;

import java.util.List;

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
  public List<AppointmentDTO> getAppointments() {
    return appointmentService.getAllAppointment();
  }

  @GetMapping("/{id}")
  public AppointmentDTO getAppointmentById (@PathVariable Long id) {
    return appointmentService.getAppointmentById(id);
  }

  @PostMapping()
  public AppointmentDTO scheduleAppointment(@RequestBody @Valid AppointmentCreateDTO appointment) {
    return appointmentService.scheduleAppointment(appointment);
  }

  @DeleteMapping("/{id}")
  public AppointmentDTO cancelAppointment(@PathVariable Long id, @RequestBody @Valid AppointmentCancelDTO appointment) {
    return appointmentService.cancelAppointment(id, appointment);
  }
  
}
