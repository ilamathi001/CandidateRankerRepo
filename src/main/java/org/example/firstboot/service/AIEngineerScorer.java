package org.example.firstboot.service;

import org.example.firstboot.model.Candidate;
import org.example.firstboot.model.Skill;

public class AIEngineerScorer {

    public double calculateScore(Candidate candidate) {

        double experienceScore = 0;   // Max 15
        double titleScore = 0;        // Max 10
        double skillScore = 0;        // Max 40
        double careerScore = 0;       // Max 20
        double behaviorScore = 0;     // Max 15

        // Experience (15)
        double exp =
                candidate.getProfile()
                        .getYears_of_experience();

        if (exp >= 5 && exp <= 9) {
            experienceScore = 15;
        } else if (exp >= 3 && exp <= 12) {
            experienceScore = 10;
        } else {
            experienceScore = 5;
        }

        // Title (10)
        String title =
                candidate.getProfile()
                        .getCurrent_title()
                        .toLowerCase();

        if (title.contains("ai engineer")) {
            titleScore += 10;
        } else if (title.contains("machine learning")) {
            titleScore += 10;
        } else if (title.contains("nlp engineer")) {
            titleScore += 8;
        } else if (title.contains("search engineer")) {
            titleScore += 8;
        } else if (title.contains("data scientist")) {
            titleScore += 6;
        }

        // Skills (40)
        for (Skill skill : candidate.getSkills()) {

            String skillName =
                    skill.getName().toLowerCase();

            if (skillName.contains("python")) {
                skillScore += 5;
            }

            if (skillName.contains("nlp")) {
                skillScore += 5;
            }

            if (skillName.contains("llm")) {
                skillScore += 5;
            }

            if (skillName.contains("retrieval")) {
                skillScore += 7;
            }

            if (skillName.contains("embedding")) {
                skillScore += 6;
            }

            if (skillName.contains("vector")) {
                skillScore += 6;
            }

            if (skillName.contains("ranking")) {
                skillScore += 6;
            }

            if (skill.getEndorsements() >= 20) {
                skillScore += 2;
            }

            if (skill.getDuration_months() >= 24) {
                skillScore += 2;
            }
        }

        skillScore = Math.min(skillScore, 40);

        // Career History (20)
        candidate.getCareer_history().forEach(ch -> {

        });

        for (var ch : candidate.getCareer_history()) {

            String desc =
                    ch.getDescription().toLowerCase();

            if (desc.contains("recommendation")) {
                careerScore += 5;
            }

            if (desc.contains("retrieval")) {
                careerScore += 5;
            }

            if (desc.contains("ranking")) {
                careerScore += 5;
            }

            if (desc.contains("search")) {
                careerScore += 5;
            }

            if (desc.contains("embeddings")) {
                careerScore += 5;
            }

            if (desc.contains("vector")) {
                careerScore += 5;
            }

            if (desc.contains("machine learning")) {
                careerScore += 3;
            }
        }

        careerScore = Math.min(careerScore, 20);

        // Behavioral Signals (15)

        if (candidate.getRedrob_signals()
                .isOpen_to_work_flag()) {

            behaviorScore += 3;
        }

        behaviorScore +=
                candidate.getRedrob_signals()
                        .getRecruiter_response_rate() * 3;

        behaviorScore +=
                candidate.getRedrob_signals()
                        .getInterview_completion_rate() * 3;

        double offerRate =
                candidate.getRedrob_signals()
                        .getOffer_acceptance_rate();

        if (offerRate > 0) {
            behaviorScore += offerRate * 2;
        }

        if (candidate.getRedrob_signals()
                .getNotice_period_days() <= 30) {

            behaviorScore += 2;
        }

        behaviorScore +=
                candidate.getRedrob_signals()
                        .getProfile_completeness_score()
                        / 100.0 * 2;

        behaviorScore = Math.min(behaviorScore, 15);

        double finalScore =
                experienceScore
                + titleScore
                + skillScore
                + careerScore
                + behaviorScore;

        return Math.round(finalScore * 100.0) / 100.0;
    }
}