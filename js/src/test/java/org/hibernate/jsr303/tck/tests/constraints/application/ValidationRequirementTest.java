package org.hibernate.jsr303.tck.tests.constraints.application;

import org.jboss.testharness.AbstractTest;

@ignore
public class ValidationRequirementTest extends AbstractTest {

    //    ClientConstraintInfo constraintInfo = new ClientConstraintInfo(new InputStreamReader(getClass()
    //            .getResourceAsStream("SecurityCheck.js")), "SecurityCheck", SecurityCheck.class.getName());
    //
    //    @Test
    //    @SpecAssertions({ @SpecAssertion(section = "3.1", id = "c"), @SpecAssertion(section = "3.1", id = "d"),
    //            @SpecAssertion(section = "3.1.1", id = "a") })
    //    public void testClassLevelConstraints() {
    //        Woman sarah = new Woman();
    //        sarah.setFirstName("Sarah");
    //        sarah.setLastName("Jones");
    //        sarah.setPersonalNumber("000000-0000");
    //
    //        Validator validator = TestUtil.getValidatorUnderTest();
    //        Set<ConstraintViolation<Woman>> violations = validator.validate(sarah);
    //        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(constraintInfo, sarah);
    //
    //        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 1); // SecurityCheck for Default in Person
    //        TestUtil.assertCorrectConstraintTypes(violations, clientViolations, SecurityCheck.class);
    //
    //        violations = validator.validate(sarah, TightSecurity.class);
    //        clientViolations = ClientValidationTestHelper.validate(constraintInfo, sarah, TightSecurity.class);
    //        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 1); // SecurityCheck for TightSecurity in Citizen
    //        TestUtil.assertCorrectConstraintTypes(violations, clientViolations, SecurityCheck.class);
    //
    //        // just to make sure - validating against a group which does not have any constraints assigned to it
    //        violations = validator.validate(sarah, DummyGroup.class);
    //        clientViolations = ClientValidationTestHelper.validate(constraintInfo, sarah, DummyGroup.class);
    //        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 0);
    //
    //        sarah.setPersonalNumber("740523-1234");
    //        violations = validator.validate(sarah);
    //        clientViolations = ClientValidationTestHelper.validate(constraintInfo, sarah);
    //        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 0);
    //
    //        violations = validator.validate(sarah, TightSecurity.class);
    //        clientViolations = ClientValidationTestHelper.validate(constraintInfo, sarah, TightSecurity.class);
    //        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 0);
    //    }
    //
    //    @Ignore("superwoman.getFirstname() return null but property is not null, causing serialization to say its null and validation on client side to be wrong")
    //    @Test
    //    @SpecAssertions({ @SpecAssertion(section = "3.1", id = "d"), @SpecAssertion(section = "3.1.2", id = "a"),
    //            @SpecAssertion(section = "3.1.2", id = "c") })
    //    public void testFieldAccess() {
    //        SuperWoman superwoman = new SuperWoman();
    //
    //        Validator validator = TestUtil.getValidatorUnderTest();
    //        Set<ConstraintViolation<SuperWoman>> violations = validator.validateProperty(superwoman, "firstName");
    //        // TODO superwoman.getFirstname() return null but property is not null
    //        // can not manage that with client validateValue();
    //        // is it necessery to manage that for client validation ?
    //        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validateValue(constraintInfo,
    //                superwoman.getClass(), "firstName", superwoman.getFirstName());
    //        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 0);
    //
    //        superwoman.setFirstName(null);
    //        violations = validator.validateProperty(superwoman, "firstName");
    //        clientViolations = ClientValidationTestHelper.validateValue(constraintInfo, superwoman.getClass(),
    //                "firstName", superwoman.getFirstName());
    //        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 1);
    //        TestUtil.assertCorrectConstraintTypes(violations, clientViolations, NotNull.class);
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
    //        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validateValue(constraintInfo,
    //                superwoman.getClass(), "lastName", superwoman.getLastName());
    //        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 0);
    //
    //        superwoman.setHiddenName(null);
    //        violations = validator.validateProperty(superwoman, "lastName");
    //        //TODO change to validateProperty ???
    //        clientViolations = ClientValidationTestHelper.validateValue(constraintInfo, superwoman.getClass(),
    //                "lastName", superwoman.getLastName());
    //        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 1);
    //        TestUtil.assertCorrectConstraintTypes(violations, clientViolations, NotNull.class);
    //    }
    //
    //    @Ignore("hibernate validator give me only 1 constraintDescriptor for Building.buildingCosts,"
    //            + "if i change the message in 1 constraint I get 2 constraint, so hibernate Validator"
    //            + " check equals on getConstraintDescriptor")
    //    @Test
    //    @SpecAssertions({ @SpecAssertion(section = "3.1.2", id = "a"), @SpecAssertion(section = "3.1.2", id = "b") })
    //    public void testConstraintAppliedOnFieldAndProperty() {
    //        Building building = new Building(10000000);
    //
    //        Validator validator = TestUtil.getValidatorUnderTest();
    //        Set<ConstraintViolation<Building>> violations = validator.validate(building);
    //        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(constraintInfo,
    //                building);
    //        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 2);
    //        String expectedMessage = "Building costs are max {max} dollars.";
    //        TestUtil.assertCorrectConstraintViolationMessages(violations, clientViolations, expectedMessage,
    //                expectedMessage);
    //    }
    //
    //    @Ignore("4 constraints apply to private fields which is not visible for serializer")
    //    @Test
    //    @SpecAssertion(section = "3.1.2", id = "e")
    //    public void testFieldAndPropertyVisibilityIsNotConstrained() {
    //        Visibility entity = new Visibility();
    //
    //        Validator validator = TestUtil.getValidatorUnderTest();
    //        Set<ConstraintViolation<Visibility>> violations = validator.validate(entity);
    //        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(constraintInfo, entity);
    //        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 6);
    //        TestUtil.assertCorrectConstraintTypes(violations, clientViolations, Min.class, Min.class, Min.class,
    //                DecimalMin.class, DecimalMin.class, DecimalMin.class);
    //        TestUtil.assertCorrectConstraintViolationMessages(violations, clientViolations, "publicField",
    //                "protectedField", "privateField", "publicProperty", "protectedProperty", "privateProperty");
    //
    //        entity.setValues(100);
    //        violations = validator.validate(entity);
    //        clientViolations = ClientValidationTestHelper.validate(constraintInfo, entity);
    //        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 0);
    //    }
    //
    //    static class StaticFieldsAndProperties {
    //        @NotNull
    //        static Object staticField = null;
    //
    //        @NotNull
    //        static Object getStaticProperty() {
    //            return null;
    //        }
    //    }
}

interface DummyGroup {
}
