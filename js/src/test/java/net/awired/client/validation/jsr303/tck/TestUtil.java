package net.awired.client.validation.jsr303.tck;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.FileAssert.fail;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;
import javax.validation.spi.ValidationProvider;
import junit.framework.Assert;
import net.awired.client.bean.validation.js.domain.ClientConstraintViolation;
import org.hibernate.jsr303.tck.util.TestUtil.PathImpl;
import org.hibernate.validator.HibernateValidator;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class TestUtil {

    private static String VALIDATION_PROVIDER_TEST_CLASS = "validation.provider";

    private static ValidationProvider<?> validationProviderUnderTest;

    private TestUtil() {
    }

    public static Validator getValidatorUnderTest() {
        return getValidatorFactoryUnderTest().getValidator();
    }

    public static ValidationProvider<?> getValidationProviderUnderTest() {
        if (validationProviderUnderTest == null) {
            instantiateValidationProviderUnderTest();
        }
        return validationProviderUnderTest;
    }

    public static ValidatorFactory getValidatorFactoryUnderTest() {
        Configuration<?> config = getConfigurationUnderTest();
        return config.buildValidatorFactory();
    }

    public static Configuration<?> getConfigurationUnderTest() {
        if (validationProviderUnderTest == null) {
            instantiateValidationProviderUnderTest();
        }

        GenericBootstrap byDefaultProvider = Validation.byDefaultProvider();
        //        ProviderSpecificBootstrap<?> bootstrap = Validation.byProvider(validationProviderUnderTest.getClass());
        return byDefaultProvider.configure();
    }

    public static InputStream getInputStreamForPath(String path) {
        // try the context class loader first
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

        // try the current class loader
        if (inputStream == null) {
            inputStream = TestUtil.class.getResourceAsStream(path);
        }
        return inputStream;
    }

    private static <U extends ValidationProvider<?>> void instantiateValidationProviderUnderTest() {
        //        String validatorProviderClassName = System.getProperty(VALIDATION_PROVIDER_TEST_CLASS);
        String validatorProviderClassName = HibernateValidator.class.getName();
        //        if (validatorProviderClassName == null) {
        //            throw new RuntimeException(
        //                    "The test harness must specify the class name of the validation provider under test. Set system property '"
        //                            + VALIDATION_PROVIDER_TEST_CLASS + "'");
        //        }

        Class<U> providerClass;
        try {
            @SuppressWarnings("unchecked")
            Class<U> tmpClazz = (Class<U>) TestUtil.class.getClassLoader().loadClass(validatorProviderClassName);
            providerClass = tmpClazz;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to load " + validatorProviderClassName);
        }

        try {
            validationProviderUnderTest = providerClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to instantiate " + validatorProviderClassName);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    public static <T> void assertServerEqualsClient(Set<ConstraintViolation<T>> serverViolations,
            Set<ClientConstraintViolation> clientViolations) {

        Assert.assertEquals(serverViolations.size(), clientViolations.size());
        for (final ConstraintViolation<T> serverViolation : serverViolations) {
            final String serverPath = serverViolation.getPropertyPath().toString();
            final String serverConstraintClassName = serverViolation.getConstraintDescriptor().getAnnotation()
                    .annotationType().getName();

            ClientConstraintViolation clientViolation = Iterables.find(clientViolations,
                    new Predicate<ClientConstraintViolation>() {
                        @Override
                        public boolean apply(ClientConstraintViolation input) {

                            if (serverPath.equals(input.getPropertyPath())
                                    && serverConstraintClassName.equals(input.getClientConstraintDescriptor()
                                            .getType())) {
                                return true;
                            }
                            return false;
                        }
                    });

            Assert.assertNotNull("client violation not found for " + serverPath + " : " + serverConstraintClassName,
                    clientViolation);

            // TODO put back
            //Assert.assertEquals(serverViolation.getMessage(), clientViolation.getMessage());
            Assert.assertEquals(serverViolation.getMessageTemplate(), clientViolation.getMessageTemplate());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////

    public static <T> void assertCorrectNumberOfViolations(Set<ConstraintViolation<T>> violations,
            Set<ClientConstraintViolation> clientViolations, int expectedViolations) {
        assertEquals(violations.size(), expectedViolations, "Wrong number of constraint violations. Expected: "
                + expectedViolations + " Actual: " + violations.size());
        assertEquals(clientViolations.size(), expectedViolations,
                "Wrong number of constraint violations for Client. Expected: " + expectedViolations + " Actual: "
                        + clientViolations.size());
    }

    ///////////////////////////////////////////////////////////////

    public static <T> void assertCorrectPropertyPaths(Set<ConstraintViolation<T>> violations,
            Set<ClientConstraintViolation> clientViolations, String... propertyPaths) {
        List<Path> propertyPathsOfViolations = new ArrayList<Path>();
        for (ConstraintViolation<?> violation : violations) {
            propertyPathsOfViolations.add(violation.getPropertyPath());
        }
        List<String> clientPropertyPathsOfViolations = new ArrayList<String>();
        for (ClientConstraintViolation violation : clientViolations) {
            clientPropertyPathsOfViolations.add(violation.getPropertyPath());
        }

        assertEquals(propertyPaths.length, propertyPathsOfViolations.size(),
                "Wrong number of property paths. Expected: " + propertyPaths.length + " Actual: "
                        + propertyPathsOfViolations.size());

        for (String propertyPath : propertyPaths) {
            Path expectedPath = PathImpl.createPathFromString(propertyPath);
            boolean containsPath = false;
            for (Path actualPath : propertyPathsOfViolations) {
                if (org.hibernate.jsr303.tck.util.TestUtil.assertEqualPaths(expectedPath, actualPath)) {
                    containsPath = true;
                    break;
                }
            }
            if (!containsPath) {
                fail(expectedPath
                        + " is not in the list of path instances contained in the actual constraint violations: "
                        + propertyPathsOfViolations);
            }
        }

        for (String propertyPath : propertyPaths) {
            Path expectedPath = PathImpl.createPathFromString(propertyPath);
            boolean containsPath = false;
            for (String actualPath : clientPropertyPathsOfViolations) {
                if (isEqualPaths(expectedPath, actualPath)) {
                    containsPath = true;
                    break;
                }
            }
            if (!containsPath) {
                fail(expectedPath
                        + " is not in the client list of path instances contained in the actual constraint violations: "
                        + clientPropertyPathsOfViolations);
            }
        }

    }

    public static <T> void assertConstraintViolation(ConstraintViolation<T> violation,
            ClientConstraintViolation clientViolation, Class<?> rootBean, Object invalidValue, String propertyPath) {
        Path expectedPath = PathImpl.createPathFromString(propertyPath);
        if (!org.hibernate.jsr303.tck.util.TestUtil.assertEqualPaths(violation.getPropertyPath(), expectedPath)) {
            fail("Property paths differ. Actual: " + violation.getPropertyPath() + " Expected: " + expectedPath);
        }
        assertEqualPaths(expectedPath, clientViolation.getPropertyPath());

        //TODO can we check the bean ?
        //        assertEquals(violation.getRootBeanClass(), rootBean, "Wrong root bean.");
        assertEquals(violation.getInvalidValue(), invalidValue, "Wrong invalid value.");
    }

    public static void assertEqualPaths(Path expected, String current) {
        Assert.assertEquals(expected.toString(), current);
    }

    public static boolean isEqualPaths(Path expected, String current) {
        return expected.toString().equals(current);
    }

    /////////////

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
