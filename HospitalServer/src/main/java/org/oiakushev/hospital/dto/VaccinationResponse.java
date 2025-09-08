package org.oiakushev.hospital.dto;

import lombok.Data;
import org.oiakushev.hospital.model.Vaccination;

import java.util.Date;

@Data
public class VaccinationResponse {
    private Long id;
    private String description;
    private Integer effectiveTime;
    private Long patientId;
    private Long personalId;
    private Date createdAt;

    public static VaccinationResponse from(Vaccination vaccination) {
        VaccinationResponse vaccinationResponse = new VaccinationResponse();

        vaccinationResponse.setId(vaccination.getId());
        vaccinationResponse.setDescription(vaccination.getDescription());
        vaccinationResponse.setEffectiveTime(vaccination.getEffectiveTime());
        vaccinationResponse.setPatientId(vaccination.getPatient().getId());
        vaccinationResponse.setPersonalId(vaccination.getPersonal().getId());
        vaccinationResponse.setCreatedAt(vaccination.getCreatedAt());

        return vaccinationResponse;
    }
}
