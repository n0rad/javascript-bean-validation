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
package fr.norad.client.bean.validation.js.service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import fr.norad.client.bean.validation.js.domain.ClientConstraintDescriptor;
import fr.norad.client.bean.validation.js.domain.ClientPropertyDescriptor;
import fr.norad.client.bean.validation.js.domain.ClientValidatorInfo;
import fr.norad.client.bean.validation.js.domain.PropertyType;
import fr.norad.core.lang.reflect.ReflectTools;

public class ValidationService {

    private ValidatorFactory validatorFactory;
    private EmptyInterpolatorContext emptyContext = new EmptyInterpolatorContext();

    public ValidationService() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    public ValidationService(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    public ClientPropertyDescriptor getValidationObject(Class<?> clazz) {
        ClientPropertyDescriptor e = new ClientPropertyDescriptor();
        fillValidationObject(clazz, e);
        return e;
    }

    public ClientValidatorInfo getValidatorInfo(Class<?> clazz, Locale locale) {
        ClientValidatorInfo info = new ClientValidatorInfo();
        info.setProperties(getValidationObject(clazz));
        info.setMessages(getMessages(info.getProperties(), locale));
        return info;
    }

    public ClientValidatorInfo getValidatorInfo(Class<?> clazz) {
        ClientValidatorInfo info = new ClientValidatorInfo();
        info.setProperties(getValidationObject(clazz));
        info.setMessages(getMessages(info.getProperties(), Locale.getDefault()));
        return info;
    }

    public ClientPropertyDescriptor getValidationObject(Class<?> clazz, String property) {
        ClientPropertyDescriptor clientDescriptor = new ClientPropertyDescriptor();
        BeanDescriptor beanDescriptor = validatorFactory.getValidator().getConstraintsForClass(clazz);
        PropertyDescriptor propertyDescriptor = beanDescriptor.getConstraintsForProperty(property);
        if (propertyDescriptor != null) {
            processConstrainedProperties(clazz, clientDescriptor, propertyDescriptor);
        }
        return clientDescriptor;
    }

    public Map<String, String> getMessages(ClientPropertyDescriptor clientDescriptor, Locale locale) {
        HashMap<String, String> res = new HashMap<String, String>();
        findMessagesRec(res, clientDescriptor, locale);
        return res;
    }

    public Map<String, String> getMessages(ClientPropertyDescriptor clientDescriptor) {
        HashMap<String, String> res = new HashMap<String, String>();
        findMessagesRec(res, clientDescriptor, Locale.getDefault());
        return res;
    }

    ///////////////////////////////////////////////////////////////////////////

    private void findMessagesRec(HashMap<String, String> res, ClientPropertyDescriptor clientDescriptor, Locale locale) {
        if (clientDescriptor == null) {
            return;
        }
        if (clientDescriptor.getConstraints() != null) {
            for (ClientConstraintDescriptor constraint : clientDescriptor.getConstraints()) {
                if (!res.containsKey(constraint.getType())) {
                    String message = constraint.getAttributes().get("message").toString();
                    String interpolation = validatorFactory.getMessageInterpolator().interpolate(message,
                            emptyContext, locale);
                    res.put(message, interpolation);
                }
            }
        }
        if (clientDescriptor.getProperties() != null) {
            for (ClientPropertyDescriptor property : clientDescriptor.getProperties().values()) {
                findMessagesRec(res, property, locale);
            }
        }
    }

    private void fillValidationObject(Class<?> clazz, ClientPropertyDescriptor clientDescriptor) {
        BeanDescriptor beanDescriptor = validatorFactory.getValidator().getConstraintsForClass(clazz);

        Set<ConstraintDescriptor<?>> beanConstraints = beanDescriptor.getConstraintDescriptors();
        for (ConstraintDescriptor<?> constraintDescriptor : beanConstraints) {
            ClientConstraintDescriptor clientConstraintDescriptor = new ClientConstraintDescriptor();
            fillClientConstraintFromServerConstraint(clientConstraintDescriptor, constraintDescriptor);
            clientDescriptor.retreaveCreatedConstraints().add(clientConstraintDescriptor);
        }

        Set<PropertyDescriptor> constrainedProperties = beanDescriptor.getConstrainedProperties();
        for (PropertyDescriptor propertyDescriptor : constrainedProperties) {
            processConstrainedProperties(clazz, clientDescriptor, propertyDescriptor);
        }
    }

    private void processConstrainedProperties(Class<?> clazz, ClientPropertyDescriptor clientDescriptor,
            PropertyDescriptor propertyDescriptor) {
        ClientPropertyDescriptor element = new ClientPropertyDescriptor();
        String propertyName = propertyDescriptor.getPropertyName();
        clientDescriptor.retreaveCreatedProperties().put(propertyName, element);

        Set<ConstraintDescriptor<?>> constraintDescriptors = propertyDescriptor.findConstraints()
                .unorderedAndMatchingGroups().getConstraintDescriptors();
        for (ConstraintDescriptor<?> constraintDescriptor : propertyDescriptor.getConstraintDescriptors()) {
            ClientConstraintDescriptor constraint = new ClientConstraintDescriptor();
            fillClientConstraintFromServerConstraint(constraint, constraintDescriptor);
            element.retreaveCreatedConstraints().add(constraint);
        }

        if (propertyDescriptor.isCascaded()) {
            Class<?> elementClass = propertyDescriptor.getElementClass();
            if (ReflectTools.classImplement(elementClass, List.class)) {
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
            } else if (ReflectTools.classImplement(elementClass, Map.class)) {
                //TODO MAP
                //TODO SET
            } else if (elementClass != String.class) {
                fillValidationObject(elementClass, element);
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
