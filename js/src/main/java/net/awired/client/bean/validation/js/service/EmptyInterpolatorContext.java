package net.awired.client.bean.validation.js.service;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.MessageInterpolator;
import javax.validation.Payload;
import javax.validation.metadata.ConstraintDescriptor;

final class EmptyInterpolatorContext implements MessageInterpolator.Context {
    @Override
    public Object getValidatedValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
        return new ConstraintDescriptor<Annotation>() {

            @Override
            public Annotation getAnnotation() {
                return null;
            }

            @Override
            public Set<Class<?>> getGroups() {
                return null;
            }

            @Override
            public Set<Class<? extends Payload>> getPayload() {
                return null;
            }

            @Override
            public List<Class<? extends ConstraintValidator<Annotation, ?>>> getConstraintValidatorClasses() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Map<String, Object> getAttributes() {
                return new HashMap<String, Object>();
            }

            @Override
            public Set<ConstraintDescriptor<?>> getComposingConstraints() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isReportAsSingleViolation() {
                // TODO Auto-generated method stub
                return false;
            }
        };
    }
}