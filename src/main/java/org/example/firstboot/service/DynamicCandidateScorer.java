
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

        double experienceScore = 0;
        double titleScore = 0;
        double skillScore = 0;

        double experience =
                candidate.getProfile()
                        .getYears_of_experience();

        String currentTitle =
                candidate.getProfile()
                        .getCurrent_title();

        // ==================================
        // Experience Relevance Score
        // ==================================

        if (experience >= jd.getMinExperience()
                && experience <= jd.getMaxExperience()) {

            experienceScore = 40;

        }
        else {

            experienceScore = 0;
        }

        // ==================================
        // Role Relevance Score
        // ==================================

        if (currentTitle != null
                && jd.getTitle() != null) {

            String candidateRole =
                    currentTitle.toLowerCase();

            String requiredRole =
                    jd.getTitle().toLowerCase();

            if (candidateRole.contains(requiredRole)) {

                titleScore = 20;

            }

            else if (
                    requiredRole.contains("ai")
                    && (
                    candidateRole.contains("machine learning")
                    || candidateRole.contains("ml")
                    || candidateRole.contains("nlp")
                    || candidateRole.contains("data scientist")
                    || candidateRole.contains("deep learning")
                    || candidateRole.contains("artificial intelligence")
            )) {

                titleScore = 15;
            }

            else if (
                    requiredRole.contains("backend")
                    && (
                    candidateRole.contains("java")
                    || candidateRole.contains("spring")
                    || candidateRole.contains("software engineer")
            )) {

                titleScore = 15;
            }
        }

        // ==================================
        // Semantic Skill Matching
        // ==================================

        int matchedSkills = 0;

        if (candidate.getSkills() != null
                && jd.getSkills() != null) {

            Map<String, List<String>> semanticSkills =
                    getSemanticSkills();

            for (Skill skill :
                    candidate.getSkills()) {

                String candidateSkill =
                        skill.getName()
                                .toLowerCase();

                for (String requiredSkill :
                        jd.getSkills()) {

                    String required =
                            requiredSkill.toLowerCase();

                    boolean matched = false;

                    // Direct Match

                    if (candidateSkill.contains(required)) {

                        matched = true;
                    }

                    // Semantic Match

                    if (!matched
                            && semanticSkills.containsKey(required)) {

                        for (String synonym :
                                semanticSkills.get(required)) {

                            if (candidateSkill.contains(
                                    synonym.toLowerCase())) {

                                matched = true;
                                break;
                            }
                        }
                    }

                    if (matched) {

                        matchedSkills++;
                        break;
                    }
                }
            }
        }

        skillScore =
                Math.min(
                        matchedSkills * 8,
                        40);

        // ==================================
        // Resume Score Calculation
        // ==================================

        double finalResumeScore;

        if (jd.getMinExperience() == 0) {

            // Fresher Hiring

            finalResumeScore =
                    (experienceScore * 0.20)
                    + (titleScore * 0.10)
                    + (skillScore * 0.70);

        }
        else {

            // Experienced Hiring

            finalResumeScore =
                    (experienceScore * 0.40)
                    + (titleScore * 0.20)
                    + (skillScore * 0.40);
        }

        return Math.min(
                Math.round(
                        finalResumeScore * 100.0)
                        / 100.0,
                100);
    }

    private Map<String, List<String>>
    getSemanticSkills() {

        Map<String, List<String>> map =
                new HashMap<>();

        map.put(
                "rag",
                Arrays.asList(
                        "retrieval",
                        "vector",
                        "vector search",
                        "faiss",
                        "pinecone",
                        "llamaindex",
                        "langchain",
                        "embeddings"));

        map.put(
                "llm",
                Arrays.asList(
                        "gpt",
                        "transformers",
                        "hugging face",
                        "qlora",
                        "fine tuning"));

        map.put(
                "nlp",
                Arrays.asList(
                        "bert",
                        "text classification",
                        "tokenization",
                        "named entity recognition"));

        map.put(
                "machine learning",
                Arrays.asList(
                        "deep learning",
                        "tensorflow",
                        "pytorch",
                        "xgboost"));

        map.put(
                "vector",
                Arrays.asList(
                        "faiss",
                        "pinecone",
                        "embeddings",
                        "vector search",
                        "vector database"));

        return map;
    }
}

