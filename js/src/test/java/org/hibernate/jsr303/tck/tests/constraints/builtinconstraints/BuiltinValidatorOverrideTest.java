/**
 *
 *     Copyright (C) Awired.net
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.hibernate.jsr303.tck.tests.constraints.builtinconstraints;


/**
 * @author Hardy Ferentschik
 */
//@Artifact(artifactType = ArtifactType.JSR303)
//public class BuiltinValidatorOverrideTest extends AbstractTest {
//
//    @Test
//    @SpecAssertion(section = "6", id = "b")
//    public void testXmlConfiguredValidatorConfigurationHasPrecedence() {
//        Configuration<?> config = TestUtil.getConfigurationUnderTest();
//        InputStream in = org.hibernate.jsr303.tck.util.TestUtil
//                .getInputStreamForPath("org/hibernate/jsr303/tck/tests/constraints/builtinconstraints/builtin-constraints-override.xml");
//        config.addMapping(in);
//        ValidatorFactory factory = config.buildValidatorFactory();
//        Validator validator = factory.getValidator();
//        ClientValidationTestHelper.buildValidationService(factory);
//
//        DummyEntity dummy = new DummyEntity();
//        Set<ConstraintViolation<DummyEntity>> violations = validator.validate(dummy);
//        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
//        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 0);
//
//        dummy.dummyProperty = "foobar";
//        violations = validator.validate(dummy);
//        clientViolations = ClientValidationTestHelper.validate(dummy);
//        TestUtil.assertCorrectNumberOfViolations(violations, clientViolations, 1);
//    }
//
//    class DummyEntity {
//        @NotNull
//        public String dummyProperty;
//    }
//}
