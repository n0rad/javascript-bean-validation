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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
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
