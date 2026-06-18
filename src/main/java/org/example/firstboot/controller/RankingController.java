
package org.example.firstboot.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.example.firstboot.jd.JobDescription;
import org.example.firstboot.model.Candidate;
import org.example.firstboot.model.CandidateScore;
import org.example.firstboot.service.BehaviorScoreCalculator;
import org.example.firstboot.service.CandidateLoaderService;
import org.example.firstboot.service.DynamicCandidateScorer;
import org.example.firstboot.service.ReasonGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RankingController {

    @GetMapping("/")
    public String dashboard() {

        return "dashboard";
    }

    @GetMapping("/results")
    public String results(Model model) throws Exception {

        CandidateLoaderService loader =
                new CandidateLoaderService();

        List<Candidate> candidates =
                loader.loadCandidates();

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

        DynamicCandidateScorer scorer =
                new DynamicCandidateScorer();

        BehaviorScoreCalculator behaviorCalculator =
                new BehaviorScoreCalculator();

        ReasonGenerator reasonGenerator =
                new ReasonGenerator();

        List<CandidateScore> rankedCandidates =
                new ArrayList<>();

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

            finalScore =
                    Math.round(
                            finalScore * 100.0)
                            / 100.0;

            CandidateScore cs =
                    new CandidateScore();

            cs.setCandidate(candidate);

            cs.setResumeScore(resumeScore);

            cs.setBehaviorScore(behaviorScore);

            cs.setFinalScore(finalScore);

            cs.setReasonForShortlisting(
                    reasonGenerator.generateReason(
                            candidate));

            rankedCandidates.add(cs);
        }

        rankedCandidates.sort(
                Comparator.comparingDouble(
                        CandidateScore::getFinalScore)
                        .reversed());

        model.addAttribute(
                "candidates",
                rankedCandidates.subList(
                        0,
                        Math.min(
                                100,
                                rankedCandidates.size())));

        return "results";
    }
}

