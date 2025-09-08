package org.oiakushev.hospital.dto;

import lombok.Data;
import org.oiakushev.hospital.model.Blood;

import java.util.Date;

@Data
public class BloodResponse {
    private Long id;
    private Double hemoglobin;
    private Double hematocrit;
    private Double meanCorpuscularVolume;
    private Double meanCorpuscularHemoglobin;
    private Long patientId;
    private Long personalId;
    private Date createdAt;

    public static BloodResponse from(Blood blood) {
        BloodResponse bloodResponse = new BloodResponse();

        bloodResponse.setId(blood.getId());
        bloodResponse.setHemoglobin(blood.getHemoglobin());
        bloodResponse.setHematocrit(blood.getHematocrit());
        bloodResponse.setMeanCorpuscularVolume(blood.getMeanCorpuscularVolume());
        bloodResponse.setMeanCorpuscularHemoglobin(blood.getMeanCorpuscularHemoglobin());
        bloodResponse.setPatientId(blood.getPatient().getId());
        bloodResponse.setPersonalId(blood.getPersonal().getId());
        bloodResponse.setCreatedAt(blood.getCreatedAt());

        return bloodResponse;
    }
}
