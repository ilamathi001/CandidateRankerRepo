package org.example.firstboot.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RankingController {

    @GetMapping("/")
    public String dashboard() {

        return "dashboard";
    }

    @GetMapping("/results")
    public String results(

            @RequestParam String title,

            @RequestParam String experienceLevel,

            Model model) {

        CandidateLoaderService loader =
                new CandidateLoaderService();

        List<Candidate> candidates;

        try {

            candidates =
                    loader.loadCandidates();

        } catch (Exception e) {

            model.addAttribute(
                    "message",
                    "Unable to load candidate profiles.");

            return "results";
        }

        JobDescription jd =
                new JobDescription();

        jd.setTitle(title);

        // Experience Mapping

        if ("fresher".equals(experienceLevel)) {

            jd.setMinExperience(0);
            jd.setMaxExperience(2);

        } else if ("1-3".equals(experienceLevel)) {

            jd.setMinExperience(1);
            jd.setMaxExperience(3);

        } else if ("3-5".equals(experienceLevel)) {

            jd.setMinExperience(3);
            jd.setMaxExperience(5);

        } else if ("5-10".equals(experienceLevel)) {

            jd.setMinExperience(5);
            jd.setMaxExperience(10);

        } else {

            jd.setMinExperience(10);
            jd.setMaxExperience(30);
        }

        // ATS Skill Mapping Removed

        jd.setSkills(
                Collections.emptyList());

        DynamicCandidateScorer scorer =
                new DynamicCandidateScorer();

        BehaviorScoreCalculator behaviorCalculator =
                new BehaviorScoreCalculator();

        ReasonGenerator reasonGenerator =
                new ReasonGenerator();

        List<CandidateScore> rankedCandidates =
                new ArrayList<>();

        for (Candidate candidate : candidates) {

            if (candidate == null
                    || candidate.getProfile() == null) {

                continue;
            }

            double experience =
                    candidate.getProfile()
                            .getYears_of_experience();

            // Eligibility Criteria

            if (experience < jd.getMinExperience()
                    || experience > jd.getMaxExperience()) {

                continue;
            }

            double resumeScore =
                    scorer.calculateResumeScore(
                            candidate,
                            jd);

            resumeScore =
                    Math.max(
                            0,
                            Math.min(
                                    100,
                                    resumeScore));

            double behaviorScore =
                    behaviorCalculator.calculate(
                            candidate);

            behaviorScore =
                    Math.max(
                            0,
                            Math.min(
                                    100,
                                    behaviorScore));

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

            cs.setResumeScore(
                    resumeScore);

            cs.setBehaviorScore(
                    behaviorScore);

            cs.setFinalScore(
                    finalScore);

            cs.setReasonForShortlisting(
                    reasonGenerator.generateReason(
                            candidate));

            rankedCandidates.add(cs);
        }

        rankedCandidates.sort(
                Comparator.comparingDouble(
                        CandidateScore::getFinalScore)
                        .reversed());

        if (rankedCandidates.isEmpty()) {

            model.addAttribute(
                    "message",
                    "No eligible candidates found for the selected experience range.");

            return "results";
        }

        try {

            CsvExporter exporter =
                    new CsvExporter();

            exporter.exportTopCandidates(
                    rankedCandidates,
                    Math.min(
                            100,
                            rankedCandidates.size()));

        } catch (Exception e) {

            System.out.println(
                    "CSV Export Failed : "
                            + e.getMessage());
        }

        model.addAttribute(
                "jobTitle",
                title);

        model.addAttribute(
                "experienceLevel",
                experienceLevel);

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