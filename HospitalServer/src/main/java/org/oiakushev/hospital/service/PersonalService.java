package org.oiakushev.hospital.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.oiakushev.hospital.dto.AuthRequest;
import org.oiakushev.hospital.dto.AuthResponse;
import org.oiakushev.hospital.dto.ChangePasswordRequest;
import org.oiakushev.hospital.dto.PersonalRequest;
import org.oiakushev.hospital.model.Personal;
import org.oiakushev.hospital.model.PersonalRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PersonalService {
    Page<Personal> getAll(Pageable pageable);
    Personal getById(Long id);
    Personal add(PersonalRequest value);
    Personal update(Long value, PersonalRequest personalRequest);
    boolean isEmpty();
    void createAdmin();
    Personal findByUsername(String username);
    void logout(HttpServletRequest request);
    Personal auth(HttpServletRequest request, PersonalRole personalRole);
    AuthResponse loginUser(AuthRequest authRequest, HttpServletRequest request, HttpServletResponse response);
    void changePassword(ChangePasswordRequest changePasswordRequest, HttpServletRequest request);
    List<Personal> search(String searchText);
}
