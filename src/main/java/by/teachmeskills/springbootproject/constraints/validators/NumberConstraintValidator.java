package by.teachmeskills.springbootproject.constraints.validators;

import by.teachmeskills.springbootproject.constraints.NumberConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumberConstraintValidator implements ConstraintValidator<NumberConstraint, String> {
    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintContext) {
        if ( id == null ) {
            return false;
        }
        return id.matches("\\d+");
    }
}
