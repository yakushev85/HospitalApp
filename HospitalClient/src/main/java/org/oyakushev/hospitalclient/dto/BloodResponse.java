package org.oyakushev.hospitalclient.dto;

import java.util.Date;

public class BloodResponse {
    private Long id;
    private Double hemoglobin;
    private Double hematocrit;
    private Double meanCorpuscularVolume;
    private Double meanCorpuscularHemoglobin;
    private Long patientId;
    private Long personalId;
    private Date createdAt;
}
