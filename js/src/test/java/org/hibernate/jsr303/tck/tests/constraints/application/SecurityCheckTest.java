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
