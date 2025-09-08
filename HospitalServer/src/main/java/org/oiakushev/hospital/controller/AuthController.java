package org.oiakushev.hospital.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.oiakushev.hospital.dto.AuthRequest;
import org.oiakushev.hospital.dto.AuthResponse;
import org.oiakushev.hospital.dto.ChangePasswordRequest;
import org.oiakushev.hospital.dto.MessageResponse;
import org.oiakushev.hospital.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private PersonalService personalService;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public AuthResponse loginUser(@RequestBody AuthRequest authRequest,
                                  HttpServletRequest request, HttpServletResponse response) {
        return personalService.loginUser(authRequest, request, response);
    }

    @RequestMapping(path="/password", method = RequestMethod.POST)
    public MessageResponse changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                          HttpServletRequest request) {
        personalService.changePassword(changePasswordRequest, request);
        return new MessageResponse("Done");
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public MessageResponse logoutUser(HttpServletRequest request) {
        personalService.logout(request);
        return new MessageResponse("Done");
    }
}
