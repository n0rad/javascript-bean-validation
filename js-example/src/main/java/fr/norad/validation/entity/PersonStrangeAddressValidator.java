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
package fr.norad.validation.entity;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import fr.norad.validation.MyNotEmpty;

public class PersonStrangeAddressValidator {

    @NotNull(message = "firstname can not be null")
    private String firstname;

    @Valid
    @NotNull
    private String lastname;

    @Valid
    private Work currentWork;

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

    @MyNotEmpty
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

    public void setCurrentWork(Work currentWork) {
        this.currentWork = currentWork;
    }

    public Work getCurrentWork() {
        return currentWork;
    }
}
