/**
 *
 *     Copyright (C) Awired.net
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
package net.awired.validation.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Payload;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import net.awired.validation.MyNotEmpty;

public class Person {

    public static class Info implements Payload {
    }

    public static class Error implements Payload {
    }

    public interface MinimumValidation {
    }

    @Size(min = 2, max = 20)
    @NotNull(message = "firstname can not be null")
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$")
    private String firstname;

    @NotNull(groups = { MinimumValidation.class, Default.class }, payload = Info.class)
    @Max(10)
    private String lastname;

    public enum Gender {
        Male, Female;
    }

    @NotNull
    private Gender gender;

    @Valid
    private Work currentWork;

    @Valid
    @Size(min = 1)
    private List<Address> addresses = new ArrayList<Address>();

    @Valid
    @Size(min = 2)
    private Map<String, Competence> languageSkill = new HashMap<String, Competence>();
    @Size(min = 2)
    private List<String> food = new ArrayList<String>();
    @Size(min = 2)
    private List<String> favoriteDayTime = new ArrayList<String>();

    public List<Address> getAddresses() {
        return addresses;
    }

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

    public void setLanguageSkill(Map<String, Competence> languageSkill) {
        this.languageSkill = languageSkill;
    }

    public Map<String, Competence> getLanguageSkill() {
        return languageSkill;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public void setFood(List<String> food) {
        this.food = food;
    }

    public List<String> getFood() {
        return food;
    }

    public void setFavoriteDayTime(List<String> favoriteDayTime) {
        this.favoriteDayTime = favoriteDayTime;
    }

    public List<String> getFavoriteDayTime() {
        return favoriteDayTime;
    }
}
