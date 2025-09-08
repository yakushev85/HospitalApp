package org.oiakushev.hospital.dto;

import lombok.Data;

@Data
public class VaccinationRequest {
    private String description;
    private Integer effectiveTime;
    private Long patientId;
    private Long personalId;
}
