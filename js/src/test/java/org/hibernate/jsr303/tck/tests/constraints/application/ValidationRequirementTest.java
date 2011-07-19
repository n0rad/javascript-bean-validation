package org.hibernate.jsr303.tck.tests.constraints.application;

import java.io.InputStreamReader;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import net.awired.client.bean.validation.js.domain.ClientConstraintViolation;
import net.awired.client.validation.jsr303.tck.TestUtil;
import net.awired.client.validation.tools.ClientConstraintInfo;
import net.awired.client.validation.tools.ClientValidationTestHelper;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.junit.Test;

public class ValidationRequirementTest extends AbstractTest {

    ClientConstraintInfo constraintInfo = new ClientConstraintInfo(new InputStreamReader(getClass()
            .getResourceAsStream("SecurityCheck.js")), "SecurityCheck", SecurityCheck.class.getName());

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.1", id = "c"), @SpecAssertion(section = "3.1", id = "d"),
            @SpecAssertion(section = "3.1.1", id = "a") })
    public void testClassLevelConstraints() {
        Woman sarah = new Woman();
        sarah.setFirstName("Sarah");
        sarah.setLastName("Jones");
        sarah.setPersonalNumber("000000-0000");

        Validator validator = TestUtil.getValidatorUnderTest();
        Set<ConstraintViolation<Woman>> violations = validator.validate(sarah);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(constraintInfo, sarah);

        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 1); // SecurityCheck for Default in Person
        TestUtil.assertCorrectConstraintTypes(violations, clientViolations, SecurityCheck.class);

        violations = validator.validate(sarah, TightSecurity.class);
        clientViolations = ClientValidationTestHelper.validate(constraintInfo, sarah, TightSecurity.class);
        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 1); // SecurityCheck for TightSecurity in Citizen
        TestUtil.assertCorrectConstraintTypes(violations, clientViolations, SecurityCheck.class);

        // just to make sure - validating against a group which does not have any constraints assigned to it
        violations = validator.validate(sarah, DummyGroup.class);
        clientViolations = ClientValidationTestHelper.validate(constraintInfo, sarah, DummyGroup.class);
        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 0);

        sarah.setPersonalNumber("740523-1234");
        violations = validator.validate(sarah);
        clientViolations = ClientValidationTestHelper.validate(constraintInfo, sarah);
        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 0);

        violations = validator.validate(sarah, TightSecurity.class);
        clientViolations = ClientValidationTestHelper.validate(constraintInfo, sarah, TightSecurity.class);
        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 0);
    }

    //    @Test
    //    @SpecAssertions({ @SpecAssertion(section = "3.1", id = "d"), @SpecAssertion(section = "3.1.2", id = "a"),
    //            @SpecAssertion(section = "3.1.2", id = "c") })
    //    public void testFieldAccess() {
    //        SuperWoman superwoman = new SuperWoman();
    //
    //        Validator validator = TestUtil.getValidatorUnderTest();
    //        Set<ConstraintViolation<SuperWoman>> violations = validator.validateProperty(superwoman, "firstName");
    //        TestUtil.assertCorrectNumberOfViolations(violations, 0);
    //
    //        superwoman.setFirstName(null);
    //        violations = validator.validateProperty(superwoman, "firstName");
    //        TestUtil.assertCorrectNumberOfViolations(violations, 1);
    //        TestUtil.assertCorrectConstraintTypes(violations, NotNull.class);
    //    }
    //
    //    @Test
    //    @SpecAssertions({ @SpecAssertion(section = "3.1", id = "d"), @SpecAssertion(section = "3.1.2", id = "a"),
    //            @SpecAssertion(section = "3.1.2", id = "d") })
    //    public void testPropertyAccess() {
    //        SuperWoman superwoman = new SuperWoman();
    //
    //        Validator validator = TestUtil.getValidatorUnderTest();
    //        Set<ConstraintViolation<SuperWoman>> violations = validator.validateProperty(superwoman, "lastName");
    //        TestUtil.assertCorrectNumberOfViolations(violations, 0);
    //
    //        superwoman.setHiddenName(null);
    //        violations = validator.validateProperty(superwoman, "lastName");
    //        TestUtil.assertCorrectNumberOfViolations(violations, 1);
    //        TestUtil.assertCorrectConstraintTypes(violations, NotNull.class);
    //    }
    //
    //    @Test
    //    @SpecAssertions({ @SpecAssertion(section = "3.1.2", id = "a"), @SpecAssertion(section = "3.1.2", id = "b") })
    //    public void testConstraintAppliedOnFieldAndProperty() {
    //        Building building = new Building(10000000);
    //
    //        Validator validator = TestUtil.getValidatorUnderTest();
    //        Set<ConstraintViolation<Building>> violations = validator.validate(building);
    //        TestUtil.assertCorrectNumberOfViolations(violations, 2);
    //        String expectedMessage = "Building costs are max {max} dollars.";
    //        TestUtil.assertCorrectConstraintViolationMessages(violations, expectedMessage, expectedMessage);
    //    }
    //
    //    @Test
    //    @SpecAssertion(section = "3.1.2", id = "e")
    //    public void testFieldAndPropertyVisibilityIsNotConstrained() {
    //        Visibility entity = new Visibility();
    //
    //        Validator validator = TestUtil.getValidatorUnderTest();
    //        Set<ConstraintViolation<Visibility>> violations = validator.validate(entity);
    //        TestUtil.assertCorrectNumberOfViolations(violations, 6);
    //        TestUtil.assertCorrectConstraintTypes(violations, Min.class, Min.class, Min.class, DecimalMin.class,
    //                DecimalMin.class, DecimalMin.class);
    //        TestUtil.assertCorrectConstraintViolationMessages(violations, "publicField", "protectedField",
    //                "privateField", "publicProperty", "protectedProperty", "privateProperty");
    //
    //        entity.setValues(100);
    //        violations = validator.validate(entity);
    //        TestUtil.assertCorrectNumberOfViolations(violations, 0);
    //    }

    static class StaticFieldsAndProperties {
        @NotNull
        static Object staticField = null;

        @NotNull
        static Object getStaticProperty() {
            return null;
        }
    }
}

interface DummyGroup {
}
