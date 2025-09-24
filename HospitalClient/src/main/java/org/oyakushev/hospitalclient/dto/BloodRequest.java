package org.oyakushev.hospitalclient.dto;

public class BloodRequest {
    private Double hemoglobin;
    private Double hematocrit;
    private Double meanCorpuscularVolume;
    private Double meanCorpuscularHemoglobin;
    private Long patientId;
    private Long personalId;

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
}
