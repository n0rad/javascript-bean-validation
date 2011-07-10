package net.awired.validation.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import net.awired.validation.MyNotEmpty;

public class Person {

    @Size(min = 5, max = 10)
    @NotNull(message = "firstname can not be null")
    private String firstname;

    @Valid
    @NotNull
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
