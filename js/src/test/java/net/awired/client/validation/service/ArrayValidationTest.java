package net.awired.client.validation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import net.awired.client.bean.validation.js.domain.ClientConstraintViolation;
import net.awired.client.validation.jsr303.tck.TestUtil;
import net.awired.client.validation.tools.ClientValidationTestHelper;
import org.junit.Test;

public class ArrayValidationTest {

    class Condition {
        @NotNull
        public String type;
    }

    class Rule {
        @Valid
        public List<Condition> conditions = new ArrayList<Condition>();

        public Rule() {
            conditions.add(new Condition());
        }
    }

    @Test
    public void should_validate_array() throws Exception {
        Validator validator = TestUtil.getValidatorUnderTest();
        Rule rule = new Rule();

        Set<ConstraintViolation<Rule>> constraintViolations = validator.validate(rule);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(rule);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 1);
        TestUtil.assertConstraintViolation(constraintViolations.iterator().next(),
                clientViolations.iterator().next(), Rule.class, null, "conditions[0].type");
    }

    @Test
    public void should_find_valid_value_in_array_object() throws Exception {
        Validator validator = TestUtil.getValidatorUnderTest();
        Rule rule = new Rule();
        rule.conditions.get(0).type = "salut!";

        Set<ConstraintViolation<Rule>> constraintViolations = validator.validate(rule);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(rule);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

}
