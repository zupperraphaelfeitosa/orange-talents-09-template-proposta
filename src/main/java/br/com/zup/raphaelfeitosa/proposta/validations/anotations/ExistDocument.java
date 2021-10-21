package br.com.zup.raphaelfeitosa.proposta.validations.anotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {ExistsDocumentValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistDocument {
    String message() default "Documento já vinculado em uma proposta";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
