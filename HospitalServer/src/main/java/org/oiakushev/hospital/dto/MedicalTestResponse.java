package org.oiakushev.hospital.dto;

import lombok.Data;
import org.oiakushev.hospital.model.MedicalTest;

import java.util.Date;

@Data
public class MedicalTestResponse {
    private Long id;
    private String description;
    private Integer result;
    private Long patientId;
    private Long personalId;
    private Date createdAt;

    public static MedicalTestResponse from(MedicalTest medicalTest) {
        MedicalTestResponse medicalTestResponse = new MedicalTestResponse();

        medicalTestResponse.setId(medicalTest.getId());
        medicalTestResponse.setDescription(medicalTest.getDescription());
        medicalTestResponse.setResult(medicalTest.getResult());
        medicalTestResponse.setPatientId(medicalTest.getPatient().getId());
        medicalTestResponse.setPersonalId(medicalTest.getPersonal().getId());
        medicalTestResponse.setCreatedAt(medicalTest.getCreatedAt());

        return medicalTestResponse;
    }
}
