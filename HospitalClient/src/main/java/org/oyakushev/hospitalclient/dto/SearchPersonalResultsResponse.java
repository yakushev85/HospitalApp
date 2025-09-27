package org.oyakushev.hospitalclient.dto;

import java.util.List;

public class SearchPersonalResultsResponse {
    private List<PersonalResponse> results;

    public List<PersonalResponse> getResults() {
        return results;
    }

    public void setResults(List<PersonalResponse> results) {
        this.results = results;
    }
}
