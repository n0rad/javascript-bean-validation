package net.awired.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FrenchZipcodeValidator implements ConstraintValidator<FrenchZipcode, String> {

    @Override
    public void initialize(FrenchZipcode constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return false;
    }

}
