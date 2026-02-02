package ifba.edu.br.medSystemAPI.validators;

import ifba.edu.br.medSystemAPI.utils.ValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CRMValidator implements ConstraintValidator<ValidCRM, String> {

  @Override
  public void initialize(ValidCRM constraintAnnotation) {
  }

  @Override
  public boolean isValid(String crm, ConstraintValidatorContext context) {
    if (crm == null || crm.trim().isEmpty()) {
      return true; // @NotBlank já valida isso
    }
    return ValidationUtils.isValidCRM(crm);
  }
}
