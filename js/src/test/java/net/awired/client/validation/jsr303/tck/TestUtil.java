package net.awired.client.validation.jsr303.tck;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import net.awired.client.bean.validation.js.domain.ClientConstraintViolation;

public class TestUtil {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static Validator getValidatorUnderTest() {
        return validatorFactory.getValidator();
    }

    public static <T> void assertCorrectNumberOfViolations(Set<ConstraintViolation<T>> violations,
            Set<ClientConstraintViolation> clientViolations, int expectedViolations) {
        assertEquals(violations.size(), expectedViolations, "Wrong number of constraint violations. Expected: "
                + expectedViolations + " Actual: " + violations.size());
        assertEquals(clientViolations.size(), expectedViolations,
                "Wrong number of constraint violations for Client. Expected: " + expectedViolations + " Actual: "
                        + clientViolations.size());
    }

    ///////////////////////////////////////////////////////////////

    public static <T> void assertCorrectConstraintTypes(Set<ConstraintViolation<T>> violations,
            Set<ClientConstraintViolation> clientViolations, Class<?>... expectedConstraintTypes) {
        assertCorrectConstraintTypesServer(violations, expectedConstraintTypes);
        assertCorrectConstraintTypesClient(clientViolations, expectedConstraintTypes);
    }

    private static <T> void assertCorrectConstraintTypesClient(Set<ClientConstraintViolation> clientViolations,
            Class<?>... expectedConstraintTypes) {
        List<String> actualConstraintTypes = new ArrayList<String>();
        for (ClientConstraintViolation violation : clientViolations) {
            actualConstraintTypes.add(violation.getClientConstraintDescriptor().getType());
        }

        assertEquals(expectedConstraintTypes.length, actualConstraintTypes.size(),
                "Wrong number of constraint types.");

        for (Class<?> expectedConstraintType : expectedConstraintTypes) {
            assertTrue(actualConstraintTypes.contains(expectedConstraintType.getName()), "The constraint type "
                    + expectedConstraintType.getName() + " is not in the list of actual violated constraint types: "
                    + actualConstraintTypes);
        }
    }

    private static <T> void assertCorrectConstraintTypesServer(Set<ConstraintViolation<T>> violations,
            Class<?>... expectedConstraintTypes) {
        List<String> actualConstraintTypes = new ArrayList<String>();
        for (ConstraintViolation<?> violation : violations) {
            actualConstraintTypes.add(((Annotation) violation.getConstraintDescriptor().getAnnotation())
                    .annotationType().getName());
        }

        assertEquals(expectedConstraintTypes.length, actualConstraintTypes.size(),
                "Wrong number of constraint types.");

        for (Class<?> expectedConstraintType : expectedConstraintTypes) {
            assertTrue(actualConstraintTypes.contains(expectedConstraintType.getName()), "The constraint type "
                    + expectedConstraintType.getName() + " is not in the list of actual violated constraint types: "
                    + actualConstraintTypes);
        }
    }

    ///////////////////////////////////////////////////////////////

    public static <T> void assertCorrectConstraintViolationMessages(Set<ConstraintViolation<T>> violations,
            Set<ClientConstraintViolation> clientViolations, String... messages) {
        assertCorrectConstraintViolationMessagesServer(violations, messages);
        assertCorrectConstraintViolationMessagesClient(clientViolations, messages);
    }

    private static <T> void assertCorrectConstraintViolationMessagesClient(
            Set<ClientConstraintViolation> clientViolations, String... messages) {
        List<String> actualMessages = new ArrayList<String>();
        for (ClientConstraintViolation violation : clientViolations) {
            actualMessages.add(violation.getMessage());
        }

        assertTrue(actualMessages.size() == messages.length, "Wrong number or error messages. Expected: "
                + messages.length + " Actual: " + actualMessages.size());

        for (String expectedMessage : messages) {
            assertTrue(actualMessages.contains(expectedMessage), "The message '" + expectedMessage
                    + "' should have been in the list of actual messages: " + actualMessages);
            actualMessages.remove(expectedMessage);
        }
        assertTrue(actualMessages.isEmpty(), "Actual messages contained more messages as specified expected messages");
    }

    private static <T> void assertCorrectConstraintViolationMessagesServer(Set<ConstraintViolation<T>> violations,
            String... messages) {
        List<String> actualMessages = new ArrayList<String>();
        for (ConstraintViolation<?> violation : violations) {
            actualMessages.add(violation.getMessage());
        }

        assertTrue(actualMessages.size() == messages.length, "Wrong number or error messages. Expected: "
                + messages.length + " Actual: " + actualMessages.size());

        for (String expectedMessage : messages) {
            assertTrue(actualMessages.contains(expectedMessage), "The message '" + expectedMessage
                    + "' should have been in the list of actual messages: " + actualMessages);
            actualMessages.remove(expectedMessage);
        }
        assertTrue(actualMessages.isEmpty(), "Actual messages contained more messages as specified expected messages");
    }

}
