package net.awired.client.bean.validation.js.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@JsonSerialize(include = Inclusion.NON_NULL)
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
