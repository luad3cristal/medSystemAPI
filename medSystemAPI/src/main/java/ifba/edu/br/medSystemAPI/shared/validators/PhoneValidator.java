package ifba.edu.br.medSystemAPI.shared.validators;

import ifba.edu.br.medSystemAPI.shared.utils.ValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

  @Override
  public void initialize(ValidPhone constraintAnnotation) {
  }

  @Override
  public boolean isValid(String phone, ConstraintValidatorContext context) {
    if (phone == null || phone.trim().isEmpty()) {
      return true; // @NotBlank já valida isso
    }
    return ValidationUtils.isValidPhone(phone);
  }
}
