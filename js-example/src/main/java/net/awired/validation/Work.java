package net.awired.validation;

import javax.validation.constraints.NotNull;

public class Work {

    @NotNull
    private String companyName;

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }
}
