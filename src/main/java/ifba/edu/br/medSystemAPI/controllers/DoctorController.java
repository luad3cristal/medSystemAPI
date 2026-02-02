package ifba.edu.br.medSystemAPI.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/doctors")
@Tag(name = "Médicos", description = "Endpoints para gerenciamento de médicos")
@SecurityRequirement(name = "bearer-key")
public class DoctorController {
  private DoctorService doctorService;

  public DoctorController(DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
  @Operation(summary = "Listar médicos ativos", description = "Retorna uma lista paginada de todos os médicos ativos no sistema. Acessível por pacientes, médicos e admin.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de médicos retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
  })
  public ResponseEntity<Page<DoctorDTO>> getActiveDoctors(
      @PageableDefault(size = 10, sort = "name") Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(doctorService.listDoctorsByStatus(pageable, true));
  }

  @GetMapping("/all")
  @Operation(summary = "Listar todos os médicos", description = "Retorna uma lista paginada de todos os médicos cadastrados no sistema (ativos e inativos)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de médicos retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
  })
  public ResponseEntity<Page<DoctorDTO>> getAllDoctors(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(doctorService.listAllDoctors(pageable));
  }

  @PostMapping
  @Operation(summary = "Cadastrar novo médico", description = "Registra um novo médico no sistema com todas as informações obrigatórias (nome, email, telefone, CRM, especialidade e endereço)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Médico cadastrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos (campos obrigatórios ausentes, formato incorreto ou especialidade inválida)", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "409", description = "Conflito - Médico já cadastrado (email ou CRM duplicado)", content = @Content(mediaType = "application/json"))
  })
  public ResponseEntity<DoctorDTO> createDoctor(@RequestBody @Valid DoctorCreateDTO doctor) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(doctorService.createDoctor(doctor));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar dados de médico", description = "Atualiza informações de um médico existente, permitindo somente as alterações de nome, telefone e endereço")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Médico atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Médico não encontrado com o ID fornecido", content = @Content(mediaType = "application/json"))
  })
  public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @RequestBody @Valid DoctorUpdateDTO doctor) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(doctorService.updateDoctor(id, doctor));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Desativar médico", description = "Realiza a exclusão de um médico, tornando-o inativo no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Médico desativado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
      @ApiResponse(responseCode = "404", description = "Médico não encontrado com o ID fornecido", content = @Content(mediaType = "application/json"))
  })
  public ResponseEntity<DoctorDTO> deactiveDoctorAccount(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(doctorService.deactiveDoctorAccount(id));
  }

}
