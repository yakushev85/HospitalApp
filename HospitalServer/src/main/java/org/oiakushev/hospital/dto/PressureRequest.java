package org.oiakushev.hospital.dto;

import lombok.Data;

@Data
public class PressureRequest {
    private Double systolic;
    private Double diastolic;
    private Long patientId;
}
