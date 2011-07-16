package net.awired.client.bean.validation.js.service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
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
import net.awired.client.bean.validation.js.domain.PropertyType;

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
                fillClientConstraintFromServerConstraint(constraint, constraintDescriptor);
                element.retreaveCreatedConstraints().add(constraint);
            }

            if (propertyDescriptor.isCascaded()) {
                Class<?> elementClass = propertyDescriptor.getElementClass();
                if (ReflectTool.classImplement(elementClass, List.class)) {
                    try {
                        Field field = clazz.getDeclaredField(propertyName);
                        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                        Class<?> elementListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
                        element.setPropertyType(PropertyType.ARRAY);
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

    private void fillClientConstraintFromServerConstraint(ClientConstraintDescriptor clientConstraint,
            ConstraintDescriptor<?> serverConstraint) {

        clientConstraint.setType(serverConstraint.getAnnotation().annotationType().getName());
        clientConstraint.setReportAsSingle(serverConstraint.isReportAsSingleViolation());
        Map<String, Object> attributes = new HashMap<String, Object>(serverConstraint.getAttributes());
        if (((Class[]) attributes.get("groups")).length == 0) {
            attributes.remove("groups");
        }
        if (((Class[]) attributes.get("payload")).length == 0) {
            attributes.remove("payload");
        }
        clientConstraint.setAttributes(attributes);

        Set<ConstraintDescriptor<?>> composingConstraints = serverConstraint.getComposingConstraints();
        if (!composingConstraints.isEmpty()) {
            Set<ClientConstraintDescriptor> constraints = new HashSet<ClientConstraintDescriptor>(
                    composingConstraints.size());
            clientConstraint.setComposingConstraints(constraints);
            for (ConstraintDescriptor<?> serverComposingConstraint : composingConstraints) {
                ClientConstraintDescriptor clientComposingConstraint = new ClientConstraintDescriptor();
                constraints.add(clientComposingConstraint);
                fillClientConstraintFromServerConstraint(clientComposingConstraint, serverComposingConstraint);
            }
        }
    }
}
