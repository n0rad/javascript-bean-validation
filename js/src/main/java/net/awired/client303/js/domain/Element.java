package net.awired.client303.js.domain;

import java.util.ArrayList;
import java.util.List;

public class Element {
    private final String name;
    private final List<Element> elements = new ArrayList<Element>();
    private final List<Constraint> constraints = new ArrayList<Constraint>();

    public Element(String name) {
        this.name = name;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public List<Element> getElements() {
        return elements;
    }

    public String getName() {
        return name;
    }
}
