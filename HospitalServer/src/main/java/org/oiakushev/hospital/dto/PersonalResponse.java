package org.oiakushev.hospital.dto;

import lombok.Data;
import org.oiakushev.hospital.model.Personal;

import java.util.Date;

@Data
public class PersonalResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String description;
    private Integer role;
    private Date createdAt;

    public static PersonalResponse fromPersonal(Personal personal) {
        PersonalResponse personalResponse = new PersonalResponse();
        personalResponse.setId(personal.getId());
        personalResponse.setUsername(personal.getUsername());
        personalResponse.setFirstName(personal.getFirstName());
        personalResponse.setLastName(personal.getLastName());
        personalResponse.setPhone(personal.getPhone());
        personalResponse.setDescription(personal.getDescription());
        personalResponse.setRole(personal.getRole());
        personalResponse.setCreatedAt(personal.getCreatedAt());

        return personalResponse;
    }
}
