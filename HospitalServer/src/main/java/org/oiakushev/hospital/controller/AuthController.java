package org.oiakushev.hospital.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.oiakushev.hospital.UserIsLockedException;
import org.oiakushev.hospital.UserNotFoundException;
import org.oiakushev.hospital.WrongPasswordException;
import org.oiakushev.hospital.dto.AuthRequest;
import org.oiakushev.hospital.dto.AuthResponse;
import org.oiakushev.hospital.dto.ChangePasswordRequest;
import org.oiakushev.hospital.dto.MessageResponse;
import org.oiakushev.hospital.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private PersonalService personalService;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public AuthResponse loginUser(@RequestBody AuthRequest authRequest,
                                  HttpServletRequest request, HttpServletResponse response)
            throws UserNotFoundException, UserIsLockedException, WrongPasswordException {
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<MessageResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(new MessageResponse("User not found."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserIsLockedException.class)
    public ResponseEntity<MessageResponse> handleUserIsLockedException(UserIsLockedException ex) {
        return new ResponseEntity<>(new MessageResponse("User is locked."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<MessageResponse> handleWrongPasswordException(WrongPasswordException ex) {
        return new ResponseEntity<>(new MessageResponse("Wrong password."), HttpStatus.BAD_REQUEST);
    }
}
