
package org.example.firstboot.service;

import org.example.firstboot.model.Candidate;
import org.example.firstboot.model.Skill;

public class ReasonGenerator {

    public String generateReason(
            Candidate candidate) {

        StringBuilder reason =
                new StringBuilder();

        double experience =
                candidate.getProfile()
                        .getYears_of_experience();

        reason.append(
                "Shortlisted due to ");

        if (experience >= 8) {

            reason.append(
                    "strong AI engineering experience, ");

        } else if (experience >= 5) {

            reason.append(
                    "relevant industry experience, ");
        }

        int count = 0;

        for (Skill skill :
                candidate.getSkills()) {

            if (count == 0) {

                reason.append(
                        "expertise in ");
            }

            reason.append(
                    skill.getName());

            count++;

            if (count >= 3) {
                break;
            }

            reason.append(", ");
        }

        reason.append(
                " and alignment with the job requirements.");

        return reason.toString();
    }
}

