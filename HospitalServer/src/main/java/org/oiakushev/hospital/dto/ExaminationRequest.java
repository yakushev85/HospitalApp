package org.oiakushev.hospital.dto;

import lombok.Data;

@Data
public class ExaminationRequest {
    private String description;
    private String diagnosis;
    private Long patientId;
}
