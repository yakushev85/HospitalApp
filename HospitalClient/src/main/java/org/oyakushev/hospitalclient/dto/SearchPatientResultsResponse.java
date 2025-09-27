package org.oyakushev.hospitalclient.dto;

import java.util.List;

public class SearchPatientResultsResponse {
    private List<PatientResponse> results;

    public List<PatientResponse> getResults() {
        return results;
    }

    public void setResults(List<PatientResponse> results) {
        this.results = results;
    }
}
