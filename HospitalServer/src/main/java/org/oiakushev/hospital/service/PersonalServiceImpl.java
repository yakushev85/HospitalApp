package org.oiakushev.hospital.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.oiakushev.hospital.UserIsLockedException;
import org.oiakushev.hospital.UserNotFoundException;
import org.oiakushev.hospital.WrongPasswordException;
import org.oiakushev.hospital.dto.AuthRequest;
import org.oiakushev.hospital.dto.AuthResponse;
import org.oiakushev.hospital.dto.ChangePasswordRequest;
import org.oiakushev.hospital.dto.PersonalRequest;
import org.oiakushev.hospital.model.Personal;
import org.oiakushev.hospital.model.PersonalRole;
import org.oiakushev.hospital.repository.JwtTokenRepository;
import org.oiakushev.hospital.repository.PageablePersonalRepository;
import org.oiakushev.hospital.repository.PersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonalServiceImpl implements PersonalService {
    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private PageablePersonalRepository pageablePersonalRepository;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Override
    public Page<Personal> getAll(Pageable pageable) {
        return pageablePersonalRepository.findAll(pageable);
    }

    @Override
    public Personal getById(Long id) {
        return personalRepository.findById(id).orElseThrow();
    }

    @Override
    public Personal add(PersonalRequest personalRequest) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Personal newPersonal = new Personal();
        newPersonal.setUsername(personalRequest.getUsername());
        newPersonal.setPassword(passwordEncoder.encode(personalRequest.getPassword()));
        newPersonal.setFirstName(personalRequest.getFirstName());
        newPersonal.setLastName(personalRequest.getLastName());
        newPersonal.setPhone(personalRequest.getPhone());
        newPersonal.setDescription(personalRequest.getDescription());
        newPersonal.setRole(personalRequest.getRole());
        return personalRepository.save(newPersonal);
    }

    @Override
    public Personal update(Long id, PersonalRequest personalRequest) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Personal modifyPersonal = getById(id);

        if (personalRequest.getUsername() != null)
            modifyPersonal.setUsername(personalRequest.getUsername());

        if (personalRequest.getPassword() != null)
            modifyPersonal.setPassword(passwordEncoder.encode(personalRequest.getPassword()));

        modifyPersonal.setFirstName(personalRequest.getFirstName());
        modifyPersonal.setLastName(personalRequest.getLastName());
        modifyPersonal.setPhone(personalRequest.getPhone());
        modifyPersonal.setDescription(personalRequest.getDescription());
        modifyPersonal.setRole(personalRequest.getRole());
        return personalRepository.save(modifyPersonal);
    }

    @Override
    public boolean isEmpty() {
        return personalRepository.count() == 0;
    }

    @Override
    public void createAdmin() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Personal adminAccount = new Personal();
        adminAccount.setUsername("admin");
        adminAccount.setPassword(passwordEncoder.encode("Change1234"));
        adminAccount.setRole(PersonalRole.Admin.getIndex());
        personalRepository.save(adminAccount);
    }

    @Override
    public Personal findByUsername(String username) {
        return personalRepository.findByUsername(username);
    }

    @Override
    public void logout(HttpServletRequest request) {
        Personal personal = jwtTokenRepository.auth(request, PersonalRole.Viewer);
        if (personal != null) {
            personal.setToken("");
            personalRepository.save(personal);
        }
    }

    @Override
    public Personal auth(HttpServletRequest request, PersonalRole personalRole) {
        return jwtTokenRepository.auth(request, personalRole);
    }

    @Override
    public AuthResponse loginUser(AuthRequest authRequest, HttpServletRequest request, HttpServletResponse response)
            throws UserNotFoundException, WrongPasswordException, UserIsLockedException {
        Personal resolvedPersonal = findByUsername(authRequest.getUsername());

        if (resolvedPersonal != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (!passwordEncoder.matches(authRequest.getPassword(), resolvedPersonal.getPassword())) {
                throw new WrongPasswordException();
            }

            if (resolvedPersonal.getRole() < 0) {
                throw new UserIsLockedException();
            }

            request.setAttribute("user", resolvedPersonal.getUsername());
            String token = jwtTokenRepository.generateToken(request);
            jwtTokenRepository.saveToken(token, request, response);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setId(resolvedPersonal.getId());
            authResponse.setUsername(resolvedPersonal.getUsername());
            authResponse.setRole(PersonalRole.fromIndex(resolvedPersonal.getRole()).toString());

            return authResponse;
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        Personal personal = jwtTokenRepository.auth(request, PersonalRole.Viewer);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), personal.getPassword())) {
            throw new IllegalArgumentException("Wrong old password.");
        }

        if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
            throw new IllegalArgumentException("New password can't be the same as current password.");
        }

        personal.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        personalRepository.save(personal);
    }

    @Override
    public List<Personal> search(String searchText) {

        String searchTxtMod = searchText.trim();

        boolean hasAlpha = false;
        boolean hasDigit = false;
        List<Personal> resultList = new ArrayList<>();

        for (int i=0;i<searchTxtMod.length();i++) {
            char c = searchTxtMod.charAt(i);

            if (Character.isDigit(c)) {
                hasDigit = true;
            }

            if (Character.isLetter(c)) {
                hasAlpha = true;
            }
        }

        if (hasAlpha && !hasDigit) {
            // search by first and last name
            String[] searchTxtArray = searchTxtMod.split("\\s");

            if (searchTxtArray.length >= 2) {
                String firstName = searchTxtArray[0];
                String lastName = searchTxtArray[1];

                return personalRepository.findByFirstNameAndLastName(firstName, lastName);
            }
        }

        if (!hasAlpha && hasDigit) {
            // search by id and phone
            List<Personal> searchedByPhone = personalRepository.findByPhone(searchTxtMod);
            if (searchedByPhone.isEmpty()) {
                personalRepository.findById(Long.parseLong(searchTxtMod)).ifPresent(resultList::add);
            } else {
                return searchedByPhone;
            }
        }

        return resultList;
    }
}
