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
package fr.norad.client.bean.validation.js.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientPropertyDescriptor {

    /**
     * null = ELEMENT
     */
    private PropertyType propertyType;

    private List<ClientConstraintDescriptor> constraints;

    private Map<String, ClientPropertyDescriptor> properties;

    /////////////////////////////////////////////////////////

    public List<ClientConstraintDescriptor> retreaveCreatedConstraints() {
        if (constraints == null) {
            constraints = new ArrayList<ClientConstraintDescriptor>();
        }
        return constraints;
    }

    public Map<String, ClientPropertyDescriptor> retreaveCreatedProperties() {
        if (properties == null) {
            properties = new HashMap<String, ClientPropertyDescriptor>();
        }
        return properties;
    }

    /////////////////////////////////////////////////////////

    public List<ClientConstraintDescriptor> getConstraints() {
        return constraints;
    }

    public Map<String, ClientPropertyDescriptor> getProperties() {
        return properties;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

}
