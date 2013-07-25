/**
 *
 *     Copyright (C) norad.fr
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
package fr.norad.client.validation.service;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import org.junit.Test;
import fr.norad.client.bean.validation.js.domain.ClientConstraintViolation;
import fr.norad.client.validation.jsr303.tck.TestUtil;
import fr.norad.client.validation.tools.ClientValidationTestHelper;
import fr.norad.client.validation.tools.ServerValidationTestHelper;

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
