package ifba.edu.br.medSystemAPI.validators;

import ifba.edu.br.medSystemAPI.utils.ValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CEPValidator implements ConstraintValidator<ValidCEP, String> {

  @Override
  public void initialize(ValidCEP constraintAnnotation) {
  }

  @Override
  public boolean isValid(String cep, ConstraintValidatorContext context) {
    if (cep == null || cep.trim().isEmpty()) {
      return true; // @NotBlank já valida isso
    }
    return ValidationUtils.isValidCEP(cep);
  }
}
