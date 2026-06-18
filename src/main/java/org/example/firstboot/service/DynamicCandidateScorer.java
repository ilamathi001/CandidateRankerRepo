
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

        double score = 0;

        if (candidate == null
                || candidate.getProfile() == null) {

            return 0;
        }

        // Experience Score

        double experience =
                candidate.getProfile()
                        .getYears_of_experience();

        if (experience >= jd.getMinExperience()
                && experience <= jd.getMaxExperience()) {

            score += 40;

        } else if (experience >= jd.getMinExperience() - 2) {

            score += 20;
        }

        // Title Score

        String title =
                candidate.getProfile()
                        .getCurrent_title();

        if (title != null
                && jd.getTitle() != null
                && title.toLowerCase()
                .contains(
                        jd.getTitle().toLowerCase())) {

            score += 20;
        }

        // Semantic Skill Matching

        if (candidate.getSkills() != null
                && jd.getSkills() != null) {

            int matchedSkills = 0;

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

                    // Direct Match

                    if (candidateSkill.contains(required)) {

                        matchedSkills++;
                        break;
                    }

                    // Semantic Match

                    if (semanticSkills
                            .containsKey(required)) {

                        for (String synonym :
                                semanticSkills.get(required)) {

                            if (candidateSkill.contains(
                                    synonym.toLowerCase())) {

                                matchedSkills++;
                                break;
                            }
                        }
                    }
                }
            }

            score += matchedSkills * 8;
        }

        return Math.min(score, 100);
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
                        "llm",
                        "transformers",
                        "hugging face",
                        "qlora",
                        "fine tuning"));

        map.put(
                "nlp",
                Arrays.asList(
                        "nlp",
                        "bert",
                        "text classification",
                        "tokenization",
                        "named entity recognition"));

        map.put(
                "machine learning",
                Arrays.asList(
                        "machine learning",
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
