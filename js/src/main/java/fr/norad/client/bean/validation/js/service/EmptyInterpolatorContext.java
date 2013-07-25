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