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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifba.edu.br.medSystemAPI.dtos.patient.request.PatientCreateDTO;
import ifba.edu.br.medSystemAPI.dtos.patient.request.PatientUpdateDTO;
import ifba.edu.br.medSystemAPI.dtos.patient.response.PatientDTO;
import ifba.edu.br.medSystemAPI.services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/patients")
@Tag(name = "Pacientes", description = "Endpoints para gerenciamento de pacientes")
public class PatientController {
  private PatientService patientService;

  public PatientController (PatientService patientService) {
    this.patientService = patientService;
  }

  @GetMapping
  @Operation(
    summary = "Listar pacientes ativos",
    description = "Retorna uma lista paginada de todos os pacientes ativos no sistema"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Lista de pacientes retornada com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Page.class)
      )
    )
  })
  public ResponseEntity<Page<PatientDTO>> getActivePatients (@PageableDefault(size = 10, sort = "name") Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
    .body(patientService.listPatientsByStatus(pageable, true));
  } 

  @GetMapping("/all")
  @Operation(
    summary = "Listar todos os pacientes",
    description = "Retorna uma lista paginada de todos os pacientes cadastrados no sistema (ativos e inativos)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Lista de pacientes retornada com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Page.class)
      )
    )
  })
  public ResponseEntity<Page<PatientDTO>> getAllPatients (@PageableDefault(size = 10, sort = "name") Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
    .body(patientService.listAllPatients(pageable));
  } 

  @PostMapping
  @Operation(
    summary = "Cadastrar novo paciente",
    description = "Registra um novo paciente no sistema com todas as informações obrigatórias (nome, email, telefone, CPF e endereço)"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Paciente cadastrado com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = PatientDTO.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Dados inválidos fornecidos (campos obrigatórios ausentes, CPF inválido ou formato incorreto)",
      content = @Content(mediaType = "application/json")
    ),
    @ApiResponse(
      responseCode = "409",
      description = "Conflito - Paciente já cadastrado (email ou CPF duplicado)",
      content = @Content(mediaType = "application/json")
    )
  })
  public ResponseEntity<PatientDTO> createPatient(@RequestBody @Valid PatientCreateDTO patient) {
    return ResponseEntity.status(HttpStatus.CREATED)
    .body(patientService.createPatient(patient));
  }

  @PutMapping("/{id}")
  @Operation(
    summary = "Atualizar dados de paciente",
    description = "Atualiza informações de um paciente existente, permitindo somente as alterações de nome, telefone e endereço"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Paciente atualizado com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = PatientDTO.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Dados inválidos fornecidos",
      content = @Content(mediaType = "application/json")
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Paciente não encontrado com o ID fornecido",
      content = @Content(mediaType = "application/json")
    )
  })
  public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody @Valid PatientUpdateDTO patient) {
    return ResponseEntity.status(HttpStatus.OK)
    .body(patientService.updatePatient(id, patient));
  }

  @DeleteMapping("/{id}")
  @Operation(
    summary = "Desativar paciente",
    description = "Realiza a exclusão de um paciente, tornando-o inativo no sistema"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Paciente desativado com sucesso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = PatientDTO.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Paciente não encontrado com o ID fornecido",
      content = @Content(mediaType = "application/json")
    )
  })
  public ResponseEntity<PatientDTO> deactivePatientAccount(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK)
    .body(patientService.deactivePatientAccount(id));
  }

}
