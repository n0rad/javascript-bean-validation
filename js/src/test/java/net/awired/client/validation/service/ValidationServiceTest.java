package net.awired.client.validation.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import net.awired.client.validation.tools.ClientValidationTestHelper;
import org.junit.Test;

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
