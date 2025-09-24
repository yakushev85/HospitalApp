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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getHemoglobin() {
        return hemoglobin;
    }

    public void setHemoglobin(Double hemoglobin) {
        this.hemoglobin = hemoglobin;
    }

    public Double getHematocrit() {
        return hematocrit;
    }

    public void setHematocrit(Double hematocrit) {
        this.hematocrit = hematocrit;
    }

    public Double getMeanCorpuscularVolume() {
        return meanCorpuscularVolume;
    }

    public void setMeanCorpuscularVolume(Double meanCorpuscularVolume) {
        this.meanCorpuscularVolume = meanCorpuscularVolume;
    }

    public Double getMeanCorpuscularHemoglobin() {
        return meanCorpuscularHemoglobin;
    }

    public void setMeanCorpuscularHemoglobin(Double meanCorpuscularHemoglobin) {
        this.meanCorpuscularHemoglobin = meanCorpuscularHemoglobin;
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
}
