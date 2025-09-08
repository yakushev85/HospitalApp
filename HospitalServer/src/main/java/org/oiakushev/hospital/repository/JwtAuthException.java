package org.oiakushev.hospital.repository;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthException extends AuthenticationException {
    public JwtAuthException(String message) {
        super(message);
    }
}
