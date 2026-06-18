package org.example.firstboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Skill {

    private String name;

    private int endorsements;

    private int duration_months;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEndorsements() {
        return endorsements;
    }

    public void setEndorsements(int endorsements) {
        this.endorsements = endorsements;
    }

    public int getDuration_months() {
        return duration_months;
    }

    public void setDuration_months(int duration_months) {
        this.duration_months = duration_months;
    }
}