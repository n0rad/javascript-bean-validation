package org.hibernate.jsr303.tck.tests.constraints.application;

import java.io.InputStreamReader;
import net.awired.client.validation.tools.CBVTestHelper;
import net.awired.client.validation.tools.ClientConstraintInfo;
import org.junit.Test;

public class SecurityCheckTest {

    @SecurityCheck(message = "toto42")
    private String test;

    @Test
    public void should_equals_js() {
        ClientConstraintInfo constraintInfo = new ClientConstraintInfo(new InputStreamReader(getClass()
                .getResourceAsStream("SecurityCheck.js")), "SecurityCheck", SecurityCheck.class.getName());

        CBVTestHelper.validateConstraintServerEqualsClientForValue(constraintInfo, SecurityCheckTest.class, "test",
                "salut");
        CBVTestHelper.validateConstraintServerEqualsClientForValue(constraintInfo, SecurityCheckTest.class, "test",
                "000000-0000");
    }
}
