package net.awired.client.bean.validation.js.domain;

import java.util.Map;

public class Constraint {
    private String type;
    private Map<String, Object> attributes;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
