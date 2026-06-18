package org.example.firstboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RedrobSignals {

    private boolean open_to_work_flag;

    private double recruiter_response_rate;

    private int notice_period_days;

    private double profile_completeness_score;

    private double interview_completion_rate;

    private double offer_acceptance_rate;

    private double github_activity_score;

    private int saved_by_recruiters_30d;

    private int search_appearance_30d;

    private boolean verified_email;

    private boolean verified_phone;

    public boolean isOpen_to_work_flag() {
        return open_to_work_flag;
    }

    public void setOpen_to_work_flag(boolean open_to_work_flag) {
        this.open_to_work_flag = open_to_work_flag;
    }

    public double getRecruiter_response_rate() {
        return recruiter_response_rate;
    }

    public void setRecruiter_response_rate(double recruiter_response_rate) {
        this.recruiter_response_rate = recruiter_response_rate;
    }

    public int getNotice_period_days() {
        return notice_period_days;
    }

    public void setNotice_period_days(int notice_period_days) {
        this.notice_period_days = notice_period_days;
    }

    public double getProfile_completeness_score() {
        return profile_completeness_score;
    }

    public void setProfile_completeness_score(
            double profile_completeness_score) {
        this.profile_completeness_score = profile_completeness_score;
    }

    public double getInterview_completion_rate() {
        return interview_completion_rate;
    }

    public void setInterview_completion_rate(
            double interview_completion_rate) {
        this.interview_completion_rate = interview_completion_rate;
    }

    public double getOffer_acceptance_rate() {
        return offer_acceptance_rate;
    }

    public void setOffer_acceptance_rate(
            double offer_acceptance_rate) {
        this.offer_acceptance_rate = offer_acceptance_rate;
    }

    public double getGithub_activity_score() {
        return github_activity_score;
    }

    public void setGithub_activity_score(
            double github_activity_score) {
        this.github_activity_score = github_activity_score;
    }

    public int getSaved_by_recruiters_30d() {
        return saved_by_recruiters_30d;
    }

    public void setSaved_by_recruiters_30d(
            int saved_by_recruiters_30d) {
        this.saved_by_recruiters_30d = saved_by_recruiters_30d;
    }

    public int getSearch_appearance_30d() {
        return search_appearance_30d;
    }

    public void setSearch_appearance_30d(
            int search_appearance_30d) {
        this.search_appearance_30d = search_appearance_30d;
    }

    public boolean isVerified_email() {
        return verified_email;
    }

    public void setVerified_email(boolean verified_email) {
        this.verified_email = verified_email;
    }

    public boolean isVerified_phone() {
        return verified_phone;
    }

    public void setVerified_phone(boolean verified_phone) {
        this.verified_phone = verified_phone;
    }
}