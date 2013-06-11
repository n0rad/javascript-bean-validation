package net.awired.client.bean.validation.js.domain;

import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientValidatorInfo {

    private ClientPropertyDescriptor properties;
    private Map<String, String> messages;

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }

    public ClientPropertyDescriptor getProperties() {
        return properties;
    }

    public void setProperties(ClientPropertyDescriptor properties) {
        this.properties = properties;
    }

}
