package net.awired.client.validation.tools;

import java.util.Set;
import javax.validation.ConstraintViolation;
import net.awired.client.bean.validation.js.domain.ClientConstraintViolation;
import net.awired.client.validation.jsr303.tck.TestUtil;

public class CBVTestHelper {

    public static <T> void validateConstraintServerEqualsClientForValue(ClientConstraintInfo constraintInfo,
            Class<T> constraintClass, String constraintProperty, Object value) {
        Set<ConstraintViolation<T>> serverViolations = ServerValidationTestHelper.validateValue(constraintClass,
                constraintProperty, value);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validateValue(constraintInfo,
                constraintClass, constraintProperty, value);

        TestUtil.assertServerEqualsClient(serverViolations, clientViolations);
    }
}
