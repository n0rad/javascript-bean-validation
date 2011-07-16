package net.awired.client.bean.validation.js.domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class ClientConstraintDescriptor {
    private String type;
    private Map<String, Object> attributes;
    private Boolean reportAsSingle;
    private Set<ClientConstraintDescriptor> composingConstraints;

    /////////////////////////////////////////////////

    public Set<ClientConstraintDescriptor> retreaveCreatedComposingConstraints() {
        if (composingConstraints == null) {
            composingConstraints = new HashSet<ClientConstraintDescriptor>();
        }
        return composingConstraints;
    }

    /////////////////////////////////////////////////

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

    public void setReportAsSingle(Boolean reportAsSingle) {
        this.reportAsSingle = reportAsSingle;
    }

    public Boolean getReportAsSingle() {
        return reportAsSingle;
    }

    public Set<ClientConstraintDescriptor> getComposingConstraints() {
        return composingConstraints;
    }

    public void setComposingConstraints(Set<ClientConstraintDescriptor> composingConstraints) {
        this.composingConstraints = composingConstraints;
    }

}
