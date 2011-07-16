package net.awired.client.validation.tools;

import net.awired.client.bean.validation.js.domain.ClientConstraintDescriptor;

public class ClientConstraintViolation {
    private Object invalidValue;
    private String message;
    private String messageTemplate;
    private String propertyPath;
    private ClientConstraintDescriptor clientConstraintDescriptor;

    private Object rootBean; // ??
    private Object leafBean; // ??

    public Object getInvalidValue() {
        return invalidValue;
    }

    public void setInvalidValue(Object invalidValue) {
        this.invalidValue = invalidValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    public void setClientConstraintDescriptor(ClientConstraintDescriptor clientConstraintDescriptor) {
        this.clientConstraintDescriptor = clientConstraintDescriptor;
    }

    public ClientConstraintDescriptor getClientConstraintDescriptor() {
        return clientConstraintDescriptor;
    }

}
