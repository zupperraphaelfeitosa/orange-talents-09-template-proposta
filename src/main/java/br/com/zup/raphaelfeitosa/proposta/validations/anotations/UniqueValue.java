package br.com.zup.raphaelfeitosa.proposta.validations.anotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = {UniqueValueValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueValue {

    Class<?> domainClass();

    String fieldName();

    String message() default "Este valor já existe!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
