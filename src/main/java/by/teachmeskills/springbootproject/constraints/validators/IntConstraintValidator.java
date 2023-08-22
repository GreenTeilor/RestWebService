package by.teachmeskills.springbootproject.constraints.validators;

import by.teachmeskills.springbootproject.constraints.IntConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IntConstraintValidator implements ConstraintValidator<IntConstraint, String> {
    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintContext) {
        if ( id == null ) {
            return false;
        }
        return id.matches("\\d+");
    }
}
