package org.oyakushev.hospitalclient.dto;

import java.util.Date;

public class MedicalTestResponse {
    private Long id;
    private String description;
    private Integer result;
    private String resultMessage;
    private Long patientId;
    private Long personalId;
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getPersonalId() {
        return personalId;
    }

    public void setPersonalId(Long personalId) {
        this.personalId = personalId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getResultMessage() {
        if (resultMessage == null) {
            if (result == null) {
                resultMessage = "Undefined";
            } else if (result == 0) {
                resultMessage = "Unknown";
            } else if (result > 0) {
                resultMessage = "Positive";
            } else {
                resultMessage = "Negative";
            }
        }

        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
