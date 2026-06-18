package org.example.firstboot.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.example.firstboot.model.Candidate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CandidateLoaderService {

    public List<Candidate> loadCandidates() throws Exception {

        List<Candidate> candidates = new ArrayList<>();

        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("candidates.jsonl");

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(is));

        ObjectMapper mapper = new ObjectMapper();

        String line;

        while ((line = reader.readLine()) != null) {

            Candidate candidate =
                    mapper.readValue(line, Candidate.class);

            candidates.add(candidate);
        }

        return candidates;
    }
}