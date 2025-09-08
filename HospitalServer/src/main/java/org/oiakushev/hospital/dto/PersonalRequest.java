package org.oiakushev.hospital.dto;

import lombok.Data;

@Data
public class PersonalRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String description;
    private Integer role;
}
