package org.example.firstboot.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.firstboot.jd.JobDescription;
import org.example.firstboot.model.Candidate;
import org.example.firstboot.model.Skill;

public class DynamicCandidateScorer {

    public double calculateResumeScore(
            Candidate candidate,
            JobDescription jd) {

        if (candidate == null
                || candidate.getProfile() == null) {

            return 0;
        }

        // ==========================
        // Experience Score
        // ==========================

        double experienceScore = 0;

        double experience =
                candidate.getProfile()
                        .getYears_of_experience();

        if (experience >= jd.getMinExperience()
                && experience <= jd.getMaxExperience()) {

            double midpoint =
                    (jd.getMinExperience()
                            + jd.getMaxExperience())
                            / 2.0;

            double gap =
                    Math.abs(
                            experience
                                    - midpoint);

            experienceScore =
                    Math.max(
                            60,
                            100 - (gap * 10));

        } else {

            experienceScore = 0;
        }

        // ==========================
        // Profile Quality
        // ==========================

        double profileScore = 0;

        if (candidate.getProfile()
                .getCurrent_title() != null) {

            profileScore += 40;
        }

        if (candidate.getSkills() != null) {

            profileScore += Math.min(
                    candidate.getSkills().size() * 2,
                    40);
        }

        if (candidate.getCareer_history() != null) {

            profileScore += Math.min(
                    candidate.getCareer_history().size() * 4,
                    20);
        }

        profileScore =
                Math.min(
                        profileScore,
                        100);

        // ==========================
        // Semantic Matching
        // ==========================

        int matchedSkills = 0;

        double endorsementScore = 0;

        Map<String, List<String>>
                semanticMap =
                getSemanticSkills();

        if (candidate.getSkills() != null
                && jd.getSkills() != null) {

            for (Skill skill :
                    candidate.getSkills()) {

                String candidateSkill =
                        skill.getName()
                                .toLowerCase();

                for (String required :
                        jd.getSkills()) {

                    String requiredSkill =
                            required.toLowerCase();

                    boolean matched = false;

                    if (candidateSkill.contains(
                            requiredSkill)) {

                        matched = true;
                    }

                    if (!matched
                            && semanticMap.containsKey(
                            requiredSkill)) {

                        for (String related :
                                semanticMap.get(
                                        requiredSkill)) {

                            if (candidateSkill.contains(
                                    related.toLowerCase())) {

                                matched = true;
                                break;
                            }
                        }
                    }

                    if (matched) {

                        matchedSkills++;

                        endorsementScore +=
                                Math.min(
                                        skill.getEndorsements(),
                                        20);

                        endorsementScore +=
                                Math.min(
                                        skill.getDuration_months()
                                                / 12.0,
                                        10);

                        break;
                    }
                }
            }
        }

        double skillMatchScore =
                Math.min(
                        matchedSkills * 15,
                        75);

        double skillStrengthScore =
                Math.min(
                        endorsementScore,
                        25);

        double skillScore =
                skillMatchScore
                        + skillStrengthScore;

        // ==========================
        // Resume Score
        // ==========================

        double resumeScore =
                (experienceScore * 0.35)
                        + (skillScore * 0.45)
                        + (profileScore * 0.20);

        return Math.round(
                resumeScore * 100.0)
                / 100.0;
    }

    private Map<String, List<String>>
    getSemanticSkills() {

        Map<String, List<String>> map =
                new HashMap<>();

        map.put(
                "rag",
                Arrays.asList(
                        "langchain",
                        "llamaindex",
                        "faiss",
                        "pinecone",
                        "embeddings"));

        map.put(
                "llm",
                Arrays.asList(
                        "gpt",
                        "transformers",
                        "huggingface"));

        map.put(
                "machine learning",
                Arrays.asList(
                        "tensorflow",
                        "pytorch",
                        "xgboost",
                        "deep learning"));

        map.put(
                "vector database",
                Arrays.asList(
                        "faiss",
                        "pinecone",
                        "chromadb",
                        "weaviate"));

        return map;
    }
}