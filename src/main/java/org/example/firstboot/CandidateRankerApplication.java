
package org.example.firstboot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.example.firstboot.export.CsvExporter;
import org.example.firstboot.jd.JobDescription;
import org.example.firstboot.model.Candidate;
import org.example.firstboot.model.CandidateScore;
import org.example.firstboot.service.BehaviorScoreCalculator;
import org.example.firstboot.service.CandidateLoaderService;
import org.example.firstboot.service.DynamicCandidateScorer;
import org.example.firstboot.service.ReasonGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CandidateRankerApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                CandidateRankerApplication.class,
                args);
    }

    @Bean
    CommandLineRunner test() {

        return args -> {

            CandidateLoaderService loader =
                    new CandidateLoaderService();

            List<Candidate> candidates =
                    loader.loadCandidates();

            System.out.println(
                    "Total Candidates : "
                            + candidates.size());

            // Job Description

            JobDescription jd =
                    new JobDescription();

            jd.setTitle("AI Engineer");

            jd.setMinExperience(5);

            jd.setMaxExperience(10);

            jd.setSkills(
                    List.of(
                            "Python",
                            "LLM",
                            "RAG",
                            "Retrieval",
                            "Vector"
                    )
            );

            // Services

            DynamicCandidateScorer scorer =
                    new DynamicCandidateScorer();

            BehaviorScoreCalculator behaviorCalculator =
                    new BehaviorScoreCalculator();

            ReasonGenerator reasonGenerator =
                    new ReasonGenerator();

            ArrayList<CandidateScore> rankedCandidates =
                    new ArrayList<>();

            // Score Candidates

            for (Candidate candidate : candidates) {

                double resumeScore =
                        scorer.calculateResumeScore(
                                candidate,
                                jd);

                double behaviorScore =
                        behaviorCalculator.calculate(
                                candidate);

                double finalScore =
                        (resumeScore * 0.80)
                                + (behaviorScore * 0.20);

                String reason =
                        reasonGenerator.generateReason(
                                candidate);

                CandidateScore cs =
                        new CandidateScore();

                cs.setCandidate(candidate);

                cs.setResumeScore(resumeScore);

                cs.setBehaviorScore(behaviorScore);

                cs.setFinalScore(finalScore);

                cs.setReasonForShortlisting(reason);

                rankedCandidates.add(cs);
            }

            // Sort Descending

            rankedCandidates.sort(
                    Comparator.comparingDouble(
                            CandidateScore::getFinalScore)
                            .reversed());

            // Top 10 Console Output

            System.out.println(
                    "\nTop 10 Ranked Candidates\n");

            for (int i = 0;
                 i < 10
                         && i < rankedCandidates.size();
                 i++) {

                CandidateScore cs =
                        rankedCandidates.get(i);

                System.out.println(
                        (i + 1)
                                + ". "
                                + cs.getCandidate()
                                .getCandidate_id()
                                + " | "
                                + cs.getCandidate()
                                .getProfile()
                                .getCurrent_title()
                                + " | Final Score = "
                                + cs.getFinalScore());

                System.out.println(
                        "Reason : "
                                + cs.getReasonForShortlisting());

                System.out.println();
            }

            // Export CSV

            CsvExporter exporter =
                    new CsvExporter();

            exporter.exportTopCandidates(
                    rankedCandidates,
                    100);

            System.out.println(
                    "\nTop 100 candidates exported to submission.csv");
        };
    }
}

