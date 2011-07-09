package net.awired.client.bean.validation.js.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Payload;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor.ConstraintFinder;
import javax.validation.metadata.PropertyDescriptor;
import net.awired.client.bean.validation.js.domain.Constraint;
import net.awired.client.bean.validation.js.domain.Element;

public class ValidationService {

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public Object getValidationObject(Class<?> clazz) {
        BeanDescriptor beanDescriptor = validatorFactory.getValidator().getConstraintsForClass(clazz);
        boolean beanConstrained = beanDescriptor.isBeanConstrained();
        System.out.println(beanConstrained);

        Element e = new Element("toto");

        Set<PropertyDescriptor> constrainedProperties = beanDescriptor.getConstrainedProperties();
        for (PropertyDescriptor propertyDescriptor : constrainedProperties) {
            Element element = new Element(propertyDescriptor.getPropertyName());
            e.getElements().add(element);
            for (ConstraintDescriptor<?> constraintDescriptor : propertyDescriptor.getConstraintDescriptors()) {
                Constraint constraint = new Constraint();
                element.getConstraints().add(constraint);
                constraint.setType(constraintDescriptor.getAnnotation().toString());

                {
                    Map<String, Object> attributes = constraintDescriptor.getAttributes();
                    Set<ConstraintDescriptor<?>> composingConstraints = constraintDescriptor
                            .getComposingConstraints();
                    List<?> constraintValidatorClasses = constraintDescriptor.getConstraintValidatorClasses();
                    Set<Class<?>> groups = constraintDescriptor.getGroups();
                    Set<Class<? extends Payload>> payload = constraintDescriptor.getPayload();
                    boolean reportAsSingleViolation = constraintDescriptor.isReportAsSingleViolation();
                    System.out.println();
                }
            }

            ConstraintFinder findConstraints = propertyDescriptor.findConstraints();
            Set<ConstraintDescriptor<?>> constraintDescriptors = findConstraints.getConstraintDescriptors();
            for (ConstraintDescriptor<?> constraintDescriptor : constraintDescriptors) {
                System.out.println(constraintDescriptor);
            }

            {
                String name = propertyDescriptor.getPropertyName();
                Class<?> elementClass = propertyDescriptor.getElementClass();
                boolean hasConstraints = propertyDescriptor.hasConstraints();
                boolean cascaded = propertyDescriptor.isCascaded();

                System.out.println();
            }
            //                        findConstraints.constraintDescriptors.iterator().next().
            //                        Element e2 = new Element(propertyDescriptor.getPropertyName());
            //                        e2.getConstraints().add()
            System.out.println("prop desc :" + propertyDescriptor);
        }

        return e;
    }
}
