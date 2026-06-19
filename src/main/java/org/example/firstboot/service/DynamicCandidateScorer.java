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
        double roleScore;
        double skillScore;

        double experience =
                candidate.getProfile()
                        .getYears_of_experience();

        String currentTitle =
                candidate.getProfile()
                        .getCurrent_title();

        // =================================
        // Experience Relevance
        // =================================

        if (experience >= jd.getMinExperience()
                && experience <= jd.getMaxExperience()) {

            experienceScore = 100;

        } else {

            experienceScore = 60;
        }

        // =================================
        // Role Alignment
        // =================================

        if (currentTitle != null
                && jd.getTitle() != null) {

            String candidateRole =
                    currentTitle.toLowerCase();

            String requiredRole =
                    jd.getTitle().toLowerCase();

            if (candidateRole.contains(requiredRole)) {

                roleScore = 100;

            } else {

                roleScore = 70;
            }

        } else {

            roleScore = 50;
        }

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
                        skillCount * 10,
                        100);

        // =================================
        // Resume Score
        // =================================

        double resumeScore =
                (experienceScore * 0.50)
                + (skillScore * 0.30)
                + (roleScore * 0.20);

        return Math.round(
                resumeScore * 100.0)
                / 100.0;
    }
}