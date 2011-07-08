package net.awired.client303.js.service;

import java.util.Set;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor.ConstraintFinder;
import javax.validation.metadata.PropertyDescriptor;
import net.awired.client303.js.domain.Constraint;
import net.awired.client303.js.domain.Element;

public class ValidationService {

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public Element getValidationObject(Class<?> clazz) {
        BeanDescriptor beanDescriptor = validatorFactory.getValidator().getConstraintsForClass(clazz);
        boolean beanConstrained = beanDescriptor.isBeanConstrained();
        System.out.println(beanConstrained);

        Element e = new Element("toto");

        Set<PropertyDescriptor> constrainedProperties = beanDescriptor.getConstrainedProperties();
        for (PropertyDescriptor propertyDescriptor : constrainedProperties) {
            Element element = new Element(propertyDescriptor.getPropertyName());
            e.getElements().add(element);
            Set<ConstraintDescriptor<?>> constraintDescriptors = propertyDescriptor.getConstraintDescriptors();
            for (ConstraintDescriptor<?> constraintDescriptor : constraintDescriptors) {
                Constraint constraint = new Constraint();
                element.getConstraints().add(constraint);
                constraint.setType(constraintDescriptor.getAnnotation().toString());
                System.out.println();
            }

            ConstraintFinder findConstraints = propertyDescriptor.findConstraints();
            //            findConstraints.
            //                        constraintDescriptors.iterator().next().
            //            Element e2 = new Element(propertyDescriptor.getPropertyName());
            //            e2.getConstraints().add()
            System.out.println("prop desc :" + propertyDescriptor);
        }

        return e;
    }
}
