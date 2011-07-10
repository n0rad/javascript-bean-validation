package net.awired.client.bean.validation.js.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Element {

    private String type;

    private final List<Constraint> constraints = new ArrayList<Constraint>();

    private final Map<String, Element> elements = new HashMap<String, Element>();

    /////////////////////////////////////////////////////////

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public Map<String, Element> getElements() {
        return elements;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
