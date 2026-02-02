package ifba.edu.br.medSystemAPI.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CRMValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCRM {
  String message() default "CRM inválido. Formato esperado: CRM/UF 000000 (ex: CRM/SP 123456)";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
