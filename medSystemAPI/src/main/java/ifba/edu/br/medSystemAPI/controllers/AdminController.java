package ifba.edu.br.medSystemAPI.controllers;

import ifba.edu.br.medSystemAPI.dtos.auth.request.UserApprovalDTO;
import ifba.edu.br.medSystemAPI.dtos.auth.response.PendingUserDTO;
import ifba.edu.br.medSystemAPI.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private final UserService userService;

  public AdminController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/pending-users")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<PendingUserDTO>> getPendingUsers() {
    List<PendingUserDTO> pendingUsers = userService.getPendingUsers();
    return ResponseEntity.ok(pendingUsers);
  }

  @PostMapping("/approve-user")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> approveUser(@RequestBody @Valid UserApprovalDTO dados) {
    if (dados.approved()) {
      userService.approveUser(dados.userId());
      return ResponseEntity.ok("Usuário aprovado com sucesso.");
    } else {
      userService.rejectUser(dados.userId());
      return ResponseEntity.ok("Usuário rejeitado e removido do sistema.");
    }
  }
}
