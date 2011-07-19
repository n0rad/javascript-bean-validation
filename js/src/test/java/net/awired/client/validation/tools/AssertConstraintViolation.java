package net.awired.client.validation.tools;

import java.util.Set;
import javax.validation.ConstraintViolation;
import junit.framework.Assert;
import net.awired.client.bean.validation.js.domain.ClientConstraintViolation;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class AssertConstraintViolation {

    public static <T> void assertServerEqualsClient(Set<ConstraintViolation<T>> serverViolations,
            Set<ClientConstraintViolation> clientViolations) {

        Assert.assertEquals(serverViolations.size(), clientViolations.size());
        for (final ConstraintViolation<T> serverViolation : serverViolations) {
            final String serverPath = serverViolation.getPropertyPath().toString();
            final String serverConstraintClassName = serverViolation.getConstraintDescriptor().getAnnotation()
                    .annotationType().getName();

            ClientConstraintViolation clientViolation = Iterables.find(clientViolations,
                    new Predicate<ClientConstraintViolation>() {
                        @Override
                        public boolean apply(ClientConstraintViolation input) {

                            if (serverPath.equals(input.getPropertyPath())
                                    && serverConstraintClassName.equals(input.getClientConstraintDescriptor()
                                            .getType())) {
                                return true;
                            }
                            return false;
                        };
                    });

            Assert.assertNotNull("client violation not found for " + serverPath + " : " + serverConstraintClassName,
                    clientViolation);

            // TODO put back
            //Assert.assertEquals(serverViolation.getMessage(), clientViolation.getMessage());
            Assert.assertEquals(serverViolation.getMessageTemplate(), clientViolation.getMessageTemplate());
        }
    }
}
