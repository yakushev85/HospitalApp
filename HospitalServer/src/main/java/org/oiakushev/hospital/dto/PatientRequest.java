package org.oiakushev.hospital.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PatientRequest {
    private String firstName;
    private String lastName;
    private Date dob;

    private String address;

    private Double height;
    private Double weight;
}
