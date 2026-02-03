package ifba.edu.br.medSystemAPI.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ADMIN,
  DOCTOR,
  PATIENT;

  @Override
  public String getAuthority() {
    return "ROLE_" + this.name();
  }
}
