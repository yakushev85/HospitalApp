package org.oiakushev.hospital.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private Long id;
    private String username;
    private String token;
    private String role;
}
