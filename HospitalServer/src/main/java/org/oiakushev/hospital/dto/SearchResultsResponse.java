package org.oiakushev.hospital.dto;

import lombok.Data;
import org.oiakushev.hospital.model.Patient;

import java.util.List;

@Data
public class SearchResultsResponse<T> {
    private List<T> results;

    public SearchResultsResponse(List<T> resultList) {
        results = resultList;
    }
}
