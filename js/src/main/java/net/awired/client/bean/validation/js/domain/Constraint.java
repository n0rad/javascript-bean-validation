package net.awired.client.bean.validation.js.domain;

public class Constraint {
    private String type;
    private String message;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
