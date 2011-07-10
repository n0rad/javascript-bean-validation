package net.awired.client.bean.validation.js.service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import net.awired.ajsl.core.lang.reflect.ReflectTool;
import net.awired.client.bean.validation.js.domain.Constraint;
import net.awired.client.bean.validation.js.domain.Element;

public class ValidationService {

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public Object getValidationObject(Class<?> clazz) {
        Element e = new Element();
        fillValidationObject(clazz, e);
        return e;
    }

    private void fillValidationObject(Class<?> clazz, Element e) {
        BeanDescriptor beanDescriptor = validatorFactory.getValidator().getConstraintsForClass(clazz);

        Set<PropertyDescriptor> constrainedProperties = beanDescriptor.getConstrainedProperties();
        for (PropertyDescriptor propertyDescriptor : constrainedProperties) {
            Element element = new Element();
            String propertyName = propertyDescriptor.getPropertyName();
            e.getElements().put(propertyName, element);

            for (ConstraintDescriptor<?> constraintDescriptor : propertyDescriptor.getConstraintDescriptors()) {
                Constraint constraint = new Constraint();
                element.getConstraints().add(constraint);

                //TODO reporting, payload, group, reportassingle
                constraint.setType(constraintDescriptor.getAnnotation().annotationType().getName());
                constraint.setAttributes(constraintDescriptor.getAttributes());
            }

            if (propertyDescriptor.isCascaded()) {
                Class<?> elementClass = propertyDescriptor.getElementClass();
                if (ReflectTool.classImplement(elementClass, List.class)) {
                    try {
                        Field field = clazz.getDeclaredField(propertyName);
                        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                        Class<?> elementListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
                        element.setType("list");
                        fillValidationObject(elementListClass, element);
                    } catch (SecurityException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (NoSuchFieldException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                } else if (ReflectTool.classImplement(elementClass, Map.class)) {
                    //TODO MAP
                    //TODO SET
                } else if (elementClass != String.class) {
                    fillValidationObject(elementClass, element);
                }
                System.out.println();
            }
        }
    }
}
