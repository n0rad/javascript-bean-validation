package net.awired.client.bean.validation.js.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientPropertyDescriptor {

    private String type;

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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Map<String, ClientPropertyDescriptor> getProperties() {
        return properties;
    }

}
