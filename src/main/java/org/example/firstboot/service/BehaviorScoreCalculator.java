package org.example.firstboot.service;

import org.example.firstboot.model.Candidate;
import org.example.firstboot.model.RedrobSignals;

public class BehaviorScoreCalculator {

public double calculate(Candidate candidate) {

    if (candidate == null
            || candidate.getRedrob_signals() == null) {

        return 0;
    }

    RedrobSignals signals =
            candidate.getRedrob_signals();

    double score = 0;

    if (signals.isOpen_to_work_flag()) {
        score += 20;
    }

    score +=
            signals.getRecruiter_response_rate()
                    * 20;

    score +=
            signals.getInterview_completion_rate()
                    * 15;

    score +=
            signals.getOffer_acceptance_rate()
                    * 10;

    if (signals.getNotice_period_days() <= 30) {
        score += 15;
    } else if (signals.getNotice_period_days() <= 60) {
        score += 8;
    }

    score +=
            signals.getProfile_completeness_score()
                    / 100.0 * 10;

    score +=
            signals.getGithub_activity_score()
                    / 100.0 * 10;

    return Math.min(score, 100);
}


}
