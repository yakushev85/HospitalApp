package org.oiakushev.hospital.dto;

import lombok.Data;
import org.oiakushev.hospital.model.Examination;

import java.util.Date;

@Data
public class ExaminationResponse {
    private Long id;
    private String description;
    private String diagnosis;
    private Long patientId;
    private Long personalId;
    private Date createdAt;

    public static ExaminationResponse from(Examination examination) {
        ExaminationResponse examinationResponse = new ExaminationResponse();

        examinationResponse.setId(examination.getId());
        examinationResponse.setDescription(examination.getDescription());
        examinationResponse.setDiagnosis(examination.getDiagnosis());
        examinationResponse.setPatientId(examination.getPatient().getId());
        examinationResponse.setPersonalId(examination.getPersonal().getId());
        examinationResponse.setCreatedAt(examination.getCreatedAt());

        return examinationResponse;
    }
}
