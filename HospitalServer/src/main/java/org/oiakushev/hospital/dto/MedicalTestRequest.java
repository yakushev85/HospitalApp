package org.oiakushev.hospital.dto;

import lombok.Data;

@Data
public class MedicalTestRequest {
    private String description;
    private Integer result;
    private Long patientId;
    private Long personalId;
}
