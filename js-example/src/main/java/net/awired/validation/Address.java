package net.awired.validation;

import javax.validation.constraints.NotNull;

public class Address {

    @NotNull
    private String street;
    @NotNull
    private String city;

    @NotNull
    @MyNotEmpty
    private String code;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
