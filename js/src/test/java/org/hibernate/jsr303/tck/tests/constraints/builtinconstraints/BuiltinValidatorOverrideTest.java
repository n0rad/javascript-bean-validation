package org.hibernate.jsr303.tck.tests.constraints.builtinconstraints;

import java.io.InputStream;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import net.awired.client.bean.validation.js.domain.ClientConstraintViolation;
import net.awired.client.validation.jsr303.tck.TestUtil;
import net.awired.client.validation.tools.ClientValidationTestHelper;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.junit.Test;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
public class BuiltinValidatorOverrideTest extends AbstractTest {

    @Test
    @SpecAssertion(section = "6", id = "b")
    public void testXmlConfiguredValidatorConfigurationHasPrecedence() {
        Configuration<?> config = TestUtil.getConfigurationUnderTest();
        InputStream in = org.hibernate.jsr303.tck.util.TestUtil
                .getInputStreamForPath("org/hibernate/jsr303/tck/tests/constraints/builtinconstraints/builtin-constraints-override.xml");
        config.addMapping(in);
        ValidatorFactory factory = config.buildValidatorFactory();
        Validator validator = factory.getValidator();
        ClientValidationTestHelper.buildValidationService(factory);

        DummyEntity dummy = new DummyEntity();
        Set<ConstraintViolation<DummyEntity>> violations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 0);

        dummy.dummyProperty = "foobar";
        violations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 1);
    }

    class DummyEntity {
        @NotNull
        public String dummyProperty;
    }
}
