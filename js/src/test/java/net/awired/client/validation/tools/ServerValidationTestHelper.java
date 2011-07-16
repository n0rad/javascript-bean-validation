package net.awired.client.validation.tools;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ServerValidationTestHelper {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static <T> Set<ConstraintViolation<T>> getServerViolations(T o) {
        Validator v = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> violations = v.validate(o);
        return violations;
    }
}
