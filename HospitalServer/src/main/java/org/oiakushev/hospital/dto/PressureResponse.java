package org.oiakushev.hospital.dto;

import lombok.Data;
import org.oiakushev.hospital.model.Pressure;

import java.util.Date;

@Data
public class PressureResponse {
    private Long id;
    private Double systolic;
    private Double diastolic;
    private Long patientId;
    private Long personalId;
    private Date createdAt;

    public static PressureResponse from(Pressure pressure) {
        PressureResponse pressureResponse = new PressureResponse();

        pressureResponse.setId(pressure.getId());
        pressureResponse.setSystolic(pressure.getSystolic());
        pressureResponse.setDiastolic(pressure.getDiastolic());
        pressureResponse.setPatientId(pressure.getPatient().getId());
        pressureResponse.setPersonalId(pressure.getPersonal().getId());
        pressureResponse.setCreatedAt(pressure.getCreatedAt());

        return pressureResponse;
    }
}
