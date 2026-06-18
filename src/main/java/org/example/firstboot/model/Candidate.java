package org.example.firstboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Candidate {

    private String candidate_id;

    private Profile profile;
    private List<Skill> skills;
    private List<CareerHistory> career_history;
    private RedrobSignals redrob_signals;
    public String getCandidate_id() {
        return candidate_id;
    }

    public void setCandidate_id(String candidate_id) {
        this.candidate_id = candidate_id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
    public List<CareerHistory> getCareer_history() {
        return career_history;
    }

    public void setCareer_history(List<CareerHistory> career_history) {
        this.career_history = career_history;
    }
    public RedrobSignals getRedrob_signals() {
        return redrob_signals;
    }

    public void setRedrob_signals(RedrobSignals redrob_signals) {
        this.redrob_signals = redrob_signals;
    }
}