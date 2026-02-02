package ifba.edu.br.medSystemAPI.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {
  String message() default "Telefone inválido. Formatos aceitos: (00) 00000-0000 ou (00) 0000-0000";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
