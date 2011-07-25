package net.awired.client.validation.service;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import net.awired.client.bean.validation.js.domain.ClientConstraintViolation;
import net.awired.client.validation.jsr303.tck.TestUtil;
import net.awired.client.validation.tools.ClientValidationTestHelper;
import net.awired.client.validation.tools.ServerValidationTestHelper;
import org.junit.Test;

public class SimpleTest {

    @Test
    public void should_find_not_null_constraint() throws Exception {
        Object o = new Object() {
            @NotNull
            public String genre;
        };

        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(o);
        Set<ConstraintViolation<Object>> serverViolations = ServerValidationTestHelper.validate(o);

        TestUtil.assertServerEqualsClient(serverViolations, clientViolations);
    }

}
