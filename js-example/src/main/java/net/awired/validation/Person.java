package net.awired.validation;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

public class Person {

    @NotNull
    private String firstname;

    @Valid
    @NotNull
    private String lastname;

    @Valid
    @Size(min = 1)
    private List<Address> addresses = new ArrayList<Address>();

    @Pattern(regexp = "-getAdddresses")
    public List<Address> getAddresses() {
        return addresses;
    }

    @Pattern(regexp = "-setAdddresses")
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @NotEmpty
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
