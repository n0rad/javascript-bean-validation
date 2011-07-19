package org.hibernate.jsr303.tck.tests.constraints.application;

import java.io.InputStreamReader;
import net.awired.client.validation.tools.CBVTestHelper;
import net.awired.client.validation.tools.ClientConstraintInfo;
import org.junit.Test;

public class SecurityCheckTest {

    @SecurityCheck(message = "toto42")
    private String test;

    @SecurityCheck(message = "toto42")
    private Woman toto;

    ClientConstraintInfo constraintInfo = new ClientConstraintInfo(new InputStreamReader(getClass()
            .getResourceAsStream("SecurityCheck.js")), "SecurityCheck", SecurityCheck.class.getName());

    @Test
    public void should_be_equals_with_simple_values() {
        CBVTestHelper.validateConstraintServerEqualsClientForValue(constraintInfo, SecurityCheckTest.class, "test",
                null);
        CBVTestHelper.validateConstraintServerEqualsClientForValue(constraintInfo, SecurityCheckTest.class, "test",
                "salut");
        CBVTestHelper.validateConstraintServerEqualsClientForValue(constraintInfo, SecurityCheckTest.class, "test",
                "000000-0000");

    }

    @Test
    public void should_be_equals_with_invalid_personal_number() {
        Woman yopla = new Woman();
        yopla.personalNumber = "000000-0000";

        CBVTestHelper.validateConstraintServerEqualsClientForValue(constraintInfo, SecurityCheckTest.class, "test",
                yopla);
    }

    @Test
    public void should_be_equals_with_valid_personal_number() {
        Woman yopla = new Woman();
        yopla.personalNumber = "000000-0001";

        CBVTestHelper.validateConstraintServerEqualsClientForValue(constraintInfo, SecurityCheckTest.class, "test",
                yopla);
    }

}
