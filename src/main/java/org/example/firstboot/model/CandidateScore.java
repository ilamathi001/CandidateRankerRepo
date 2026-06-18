package org.example.firstboot.model;

public class CandidateScore {

    private Candidate candidate;

    private double resumeScore;

    private double behaviorScore;

    private double finalScore;

    private String reasonForShortlisting;

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public double getResumeScore() {
        return resumeScore;
    }

    public void setResumeScore(double resumeScore) {
        this.resumeScore = resumeScore;
    }

    public double getBehaviorScore() {
        return behaviorScore;
    }

    public void setBehaviorScore(double behaviorScore) {
        this.behaviorScore = behaviorScore;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

    public String getReasonForShortlisting() {
        return reasonForShortlisting;
    }

    public void setReasonForShortlisting(String reasonForShortlisting) {
        this.reasonForShortlisting = reasonForShortlisting;
    }
}