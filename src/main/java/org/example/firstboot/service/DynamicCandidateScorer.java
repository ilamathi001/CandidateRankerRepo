
package org.example.firstboot.service;

import org.example.firstboot.jd.JobDescription;
import org.example.firstboot.model.Candidate;
import org.example.firstboot.model.Skill;

public class DynamicCandidateScorer {

    public double calculateResumeScore(
            Candidate candidate,
            JobDescription jd) {

        double score = 0;

        if (candidate == null
                || candidate.getProfile() == null) {

            return 0;
        }

        // Experience Score (40)

        double experience =
                candidate.getProfile()
                        .getYears_of_experience();

        if (experience >= jd.getMinExperience()
                && experience <= jd.getMaxExperience()) {

            score += 40;

        } else if (experience >= jd.getMinExperience() - 2) {

            score += 20;
        }

        // Title Score (30)

        String title =
                candidate.getProfile()
                        .getCurrent_title()
                        .toLowerCase();

        if (title.contains("ai engineer")) {
            score += 30;
        }

        else if (title.contains("machine learning")) {
            score += 25;
        }

        else if (title.contains("ml engineer")) {
            score += 25;
        }

        else if (title.contains("nlp engineer")) {
            score += 20;
        }

        else if (title.contains("data scientist")) {
            score += 15;
        }

        // Penalty Titles

        if (title.contains("hr")) {
            score -= 40;
        }

        if (title.contains("operations")) {
            score -= 40;
        }

        if (title.contains("sales")) {
            score -= 40;
        }

        if (title.contains("marketing")) {
            score -= 40;
        }

        // Skills Score (30)

        if (candidate.getSkills() != null
                && jd.getSkills() != null) {

            int matchedSkills = 0;

            for (Skill skill :
                    candidate.getSkills()) {

                String candidateSkill =
                        skill.getName()
                                .toLowerCase();

                for (String requiredSkill :
                        jd.getSkills()) {

                    if (candidateSkill.contains(
                            requiredSkill.toLowerCase())) {

                        matchedSkills++;
                        break;
                    }
                }
            }

            score += matchedSkills * 6;
        }

        if (score < 0) {
            score = 0;
        }

        return Math.min(score, 100);
    }
}

