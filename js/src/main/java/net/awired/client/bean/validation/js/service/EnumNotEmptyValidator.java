package net.awired.client.bean.validation.js.service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.awired.client.bean.validation.js.constraint.EnumNotEmpty;

/**
 * used by client side to check that enum element is not empty
 */
public class EnumNotEmptyValidator implements ConstraintValidator<EnumNotEmpty, Enum<?>> {

    @Override
    public void initialize(EnumNotEmpty constraintAnnotation) {
    }

    @Override
    public boolean isValid(Enum<?> object, ConstraintValidatorContext constraintValidatorContext) {
        return object.name().length() > 0;
    }

}
