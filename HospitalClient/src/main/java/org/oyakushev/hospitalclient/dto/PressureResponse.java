package org.oyakushev.hospitalclient.dto;

import java.util.Date;

public class PressureResponse {
    private Long id;
    private Double systolic;
    private Double diastolic;
    private Long patientId;
    private Long personalId;
    private Date createdAt;
}
