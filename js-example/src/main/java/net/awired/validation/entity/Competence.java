package net.awired.validation.entity;

import javax.validation.constraints.Min;

public class Competence {
    @Min(0)
    private int rating;

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }
}
