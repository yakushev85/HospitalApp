package org.oiakushev.hospital.dto;

import lombok.Data;

@Data
public class BloodRequest {
    private Double hemoglobin;
    private Double hematocrit;
    private Double meanCorpuscularVolume;
    private Double meanCorpuscularHemoglobin;
    private Long patientId;
}
