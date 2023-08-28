package by.teachmeskills.springbootproject.constraints;

import by.teachmeskills.springbootproject.constraints.validators.NumberConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = NumberConstraintValidator.class)
@Documented
public @interface NumberConstraint {

    String message() default "Not a number";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
