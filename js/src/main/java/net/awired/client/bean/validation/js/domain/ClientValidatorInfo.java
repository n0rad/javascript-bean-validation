package net.awired.client.bean.validation.js.domain;

import java.util.Map;

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
