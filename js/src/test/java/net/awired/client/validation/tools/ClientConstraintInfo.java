package net.awired.client.validation.tools;

import java.io.Reader;

public class ClientConstraintInfo {
    private Reader jsConstraint;
    private String jsConstraintName;
    private String constraintType;

    public ClientConstraintInfo(Reader jsConstraint, String jsConstraintName, String constraintType) {
        this.constraintType = constraintType;
        this.jsConstraintName = jsConstraintName;
        this.jsConstraint = jsConstraint;
    }

    public Reader getJsConstraint() {
        return jsConstraint;
    }

    public void setJsConstraint(Reader jsConstraint) {
        this.jsConstraint = jsConstraint;
    }

    public String getJsConstraintName() {
        return jsConstraintName;
    }

    public void setJsConstraintName(String jsConstraintName) {
        this.jsConstraintName = jsConstraintName;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }
}
