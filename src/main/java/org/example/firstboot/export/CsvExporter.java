
package org.example.firstboot.export;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.example.firstboot.model.CandidateScore;

public class CsvExporter {

    public void exportTopCandidates(
            List<CandidateScore> rankedCandidates,
            int topN) {

        try {

            FileWriter writer =
                    new FileWriter("submission.csv");

            writer.write(
                    "rank,candidate_id,current_title,resume_score,behavior_score,final_score,reason_for_shortlisting\n");

            for (int i = 0;
                 i < topN
                         && i < rankedCandidates.size();
                 i++) {

                CandidateScore cs =
                        rankedCandidates.get(i);

                writer.write(
                        (i + 1)
                                + ","
                                + cs.getCandidate()
                                        .getCandidate_id()
                                + ","
                                + "\""
                                + cs.getCandidate()
                                        .getProfile()
                                        .getCurrent_title()
                                + "\""
                                + ","
                                + String.format(
                                        "%.2f",
                                        cs.getResumeScore())
                                + ","
                                + String.format(
                                        "%.2f",
                                        cs.getBehaviorScore())
                                + ","
                                + String.format(
                                        "%.2f",
                                        cs.getFinalScore())
                                + ",\""
                                + cs.getReasonForShortlisting()
                                + "\"\n");
            }

            writer.close();

            System.out.println(
                    "CSV exported successfully");

        } catch (IOException e) {

            System.out.println(
                    "Error exporting CSV");

            e.printStackTrace();
        }
    }
}
