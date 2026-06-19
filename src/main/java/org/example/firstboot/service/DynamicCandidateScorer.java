package org.example.firstboot.service;

import org.example.firstboot.jd.JobDescription;
import org.example.firstboot.model.Candidate;

public class DynamicCandidateScorer {

    public double calculateResumeScore(
            Candidate candidate,
            JobDescription jd) {

        if (candidate == null
                || candidate.getProfile() == null) {

            return 0;
        }

        double experienceScore;
        double profileScore;
        double skillScore;

        double experience =
                candidate.getProfile()
                        .getYears_of_experience();

        // =================================
        // Experience Relevance
        // =================================

        double midPoint =
                (jd.getMinExperience()
                + jd.getMaxExperience()) / 2.0;

        double difference =
                Math.abs(
                        experience - midPoint);

        experienceScore =
                Math.max(
                        50,
                        100 - (difference * 10));

        // =================================
        // Profile Completeness
        // =================================

        profileScore = 20;

        if (candidate.getProfile()
                .getCurrent_title() != null
                && !candidate.getProfile()
                .getCurrent_title().isBlank()) {

            profileScore += 15;
        }

        if (candidate.getSkills() != null
                && !candidate.getSkills().isEmpty()) {

            profileScore += 15;
        }

        if (candidate.getProfile()
                .getYears_of_experience() > 0) {

            profileScore += 10;
        }

        profileScore =
                Math.min(
                        profileScore,
                        60);

        // =================================
        // Skill Diversity
        // =================================

        int skillCount = 0;

        if (candidate.getSkills() != null) {

            skillCount =
                    candidate.getSkills().size();
        }

        skillScore =
                Math.min(
                        skillCount * 8,
                        100);

        // =================================
        // Resume Score
        // =================================

        double resumeScore =
                (experienceScore * 0.50)
                + (profileScore * 0.20)
                + (skillScore * 0.30);

        return Math.round(
                resumeScore * 100.0)
                / 100.0;
    }
}