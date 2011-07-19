package net.awired.client.validation.tools;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ServerValidationTestHelper {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static <T> Set<ConstraintViolation<T>> validate(T o) {
        Validator v = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> violations = v.validate(o);
        return violations;
    }

    public static <T> Set<ConstraintViolation<T>> validateValue(Class<T> o, String propertyName, Object value,
            Class<?>... groups) {
        Validator v = validatorFactory.getValidator();
        return v.validateValue(o, propertyName, value, groups);
    }
}
