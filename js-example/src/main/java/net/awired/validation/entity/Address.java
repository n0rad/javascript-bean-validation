package net.awired.validation.entity;

import net.awired.validation.MyNotEmpty;

public class Address {

    @MyNotEmpty
    private String street;
    @MyNotEmpty
    private String city;
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
