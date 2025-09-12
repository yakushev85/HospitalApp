package org.oyakushev.hospitalclient.dto;

import java.util.Date;

public class MedicalTestResponse {
    private Long id;
    private String description;
    private Integer result;
    private Long patientId;
    private Long personalId;
    private Date createdAt;
}
