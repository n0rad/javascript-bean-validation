package net.awired.client.bean.validation.js.domain;

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
