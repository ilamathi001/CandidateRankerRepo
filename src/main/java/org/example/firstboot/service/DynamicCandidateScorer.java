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
        double skillScore = 0;
        double profileScore = 0;

        double experience =
                candidate.getProfile()
                        .getYears_of_experience();

        // =================================
        // Experience Relevance
        // =================================

        if (experience >= jd.getMinExperience()
                && experience <= jd.getMaxExperience()) {

            experienceScore = 100;

        } else {

            experienceScore = 0;
        }

        // =================================
        // Profile Quality
        // =================================

        profileScore = 40;

        if (candidate.getProfile()
                .getCurrent_title() != null
                && !candidate.getProfile()
                .getCurrent_title().isBlank()) {

            profileScore += 30;
        }

        if (candidate.getSkills() != null
                && !candidate.getSkills().isEmpty()) {

            profileScore += 30;
        }

        // =================================
        // Semantic Skill Matching
        // =================================

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

        // Max skill score = 60

        skillScore =
                Math.min(
                        matchedSkills * 12,
                        60);

        // =================================
        // Resume Score
        // =================================

        double resumeScore =
                (experienceScore * 0.40)
                + (skillScore * 0.40)
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
                        "embeddings",
                        "vector search",
                        "retrieval"));

        map.put(
                "llm",
                Arrays.asList(
                        "gpt",
                        "transformers",
                        "hugging face",
                        "fine tuning",
                        "prompt engineering"));

        map.put(
                "machine learning",
                Arrays.asList(
                        "tensorflow",
                        "pytorch",
                        "xgboost",
                        "deep learning",
                        "supervised learning"));

        map.put(
                "nlp",
                Arrays.asList(
                        "bert",
                        "tokenization",
                        "named entity recognition",
                        "text classification"));

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