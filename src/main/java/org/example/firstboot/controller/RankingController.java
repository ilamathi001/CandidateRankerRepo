
package org.example.firstboot.controller;

import java.util.ArrayList;
import java.util.Arrays;
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

            Model model) throws Exception {

        CandidateLoaderService loader =
                new CandidateLoaderService();

        List<Candidate> candidates =
                loader.loadCandidates();

        JobDescription jd =
                new JobDescription();

        jd.setTitle(title);

        // Experience Mapping

        if (experienceLevel.equals("fresher")) {

            jd.setMinExperience(0);
            jd.setMaxExperience(2);

        }
        else if (experienceLevel.equals("1-3")) {

            jd.setMinExperience(1);
            jd.setMaxExperience(3);

        }
        else if (experienceLevel.equals("3-5")) {

            jd.setMinExperience(3);
            jd.setMaxExperience(5);

        }
        else if (experienceLevel.equals("5-10")) {

            jd.setMinExperience(5);
            jd.setMaxExperience(10);

        }
        else {

            jd.setMinExperience(10);
            jd.setMaxExperience(30);
        }

        jd.setSkills(
                getSkillsForRole(title));

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

            // Experience Eligibility Filter

            double experience =
                    candidate.getProfile()
                            .getYears_of_experience();

            if (experience < jd.getMinExperience()
                    || experience > jd.getMaxExperience()) {

                continue;
            }

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
                    "No matching candidates found");

            return "results";
        }

        CsvExporter exporter =
                new CsvExporter();

        exporter.exportTopCandidates(
                rankedCandidates,
                Math.min(
                        100,
                        rankedCandidates.size()));

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

    private List<String> getSkillsForRole(
            String role) {

        role = role.toLowerCase();

        if (role.contains("ai")) {

            return Arrays.asList(
                    "Python",
                    "LLM",
                    "RAG",
                    "Machine Learning",
                    "Deep Learning");
        }

        if (role.contains("machine learning")) {

            return Arrays.asList(
                    "Python",
                    "TensorFlow",
                    "PyTorch",
                    "Machine Learning",
                    "Deep Learning");
        }

        if (role.contains("data")) {

            return Arrays.asList(
                    "Python",
                    "Statistics",
                    "SQL",
                    "Machine Learning");
        }

        if (role.contains("backend")) {

            return Arrays.asList(
                    "Java",
                    "Spring Boot",
                    "Microservices",
                    "SQL");
        }

        if (role.contains("frontend")) {

            return Arrays.asList(
                    "React",
                    "JavaScript",
                    "HTML",
                    "CSS");
        }

        return Arrays.asList();
    }
}

