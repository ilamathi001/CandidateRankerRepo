package org.example.firstboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile {

    private String current_title;

    private double years_of_experience;

    public String getCurrent_title() {
        return current_title;
    }

    public void setCurrent_title(String current_title) {
        this.current_title = current_title;
    }

    public double getYears_of_experience() {
        return years_of_experience;
    }

    public void setYears_of_experience(double years_of_experience) {
        this.years_of_experience = years_of_experience;
    }
}