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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import org.junit.Test;
import fr.norad.client.validation.tools.ClientValidationTestHelper;

public class ValidationServiceTest {

    public class Toto {
        @NotNull
        public String tot;
    }

    public class Bean {

        public Long id;

        @NotNull
        public String firstname;

        @NotNull
        public String lastname;

        @Past
        public Date birthday;

        @Pattern(regexp = "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,4}")
        public String email;

        @Valid
        public List<Toto> totos;
    }

    @Test
    public void should() throws Exception {
        Bean b = new Bean();
        b.totos = new ArrayList<ValidationServiceTest.Toto>();
        b.totos.add(new Toto());
        Object clientViolations = ClientValidationTestHelper.validate(b);
        //        Set<ConstraintViolation<Object>> serverViolations = ServerValidationTestHelper.getServerViolations(b);
        System.out.println();
    }
}
