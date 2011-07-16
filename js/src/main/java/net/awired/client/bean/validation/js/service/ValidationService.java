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
import net.awired.client.bean.validation.js.domain.ClientConstraintDescriptor;
import net.awired.client.bean.validation.js.domain.ClientPropertyDescriptor;

public class ValidationService {

    ValidatorFactory validatorFactory;

    public ValidationService() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    public ValidationService(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    public Object getValidationObject(Class<?> clazz) {
        ClientPropertyDescriptor e = new ClientPropertyDescriptor();
        fillValidationObject(clazz, e);
        return e;
    }

    ///////////////////////////////////////////////////////////////////////////

    private void fillValidationObject(Class<?> clazz, ClientPropertyDescriptor e) {
        BeanDescriptor beanDescriptor = validatorFactory.getValidator().getConstraintsForClass(clazz);

        Set<PropertyDescriptor> constrainedProperties = beanDescriptor.getConstrainedProperties();
        for (PropertyDescriptor propertyDescriptor : constrainedProperties) {
            ClientPropertyDescriptor element = new ClientPropertyDescriptor();
            String propertyName = propertyDescriptor.getPropertyName();
            e.retreaveCreatedProperties().put(propertyName, element);

            for (ConstraintDescriptor<?> constraintDescriptor : propertyDescriptor.getConstraintDescriptors()) {
                ClientConstraintDescriptor constraint = new ClientConstraintDescriptor();
                element.retreaveCreatedConstraints().add(constraint);

                //TODO reporting, payload, group, reportassingle
                constraint.setType(constraintDescriptor.getAnnotation().annotationType().getName());
                Map<String, Object> attributes = constraintDescriptor.getAttributes();
                constraint.setAttributes(attributes);
            }

            if (propertyDescriptor.isCascaded()) {
                Class<?> elementClass = propertyDescriptor.getElementClass();
                if (ReflectTool.classImplement(elementClass, List.class)) {
                    try {
                        Field field = clazz.getDeclaredField(propertyName);
                        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                        Class<?> elementListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
                        element.setType("array");
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
            }
        }
    }
}
