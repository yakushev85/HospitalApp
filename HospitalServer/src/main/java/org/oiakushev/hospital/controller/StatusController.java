package org.oiakushev.hospital.controller;

import org.oiakushev.hospital.dto.MessageResponse;
import org.oiakushev.hospital.service.PatientService;
import org.oiakushev.hospital.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private PersonalService personalService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public MessageResponse getStatus() {
        if (personalService.isEmpty()) {
            patientService.createDummy();
            personalService.createAdmin();
            return new MessageResponse("Initialized");
        } else {
            return new MessageResponse("Ready");
        }
    }
}
