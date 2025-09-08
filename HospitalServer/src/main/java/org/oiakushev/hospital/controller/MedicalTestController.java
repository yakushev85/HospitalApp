package org.oiakushev.hospital.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.oiakushev.hospital.dto.MedicalTestRequest;
import org.oiakushev.hospital.dto.MedicalTestResponse;
import org.oiakushev.hospital.model.PersonalRole;
import org.oiakushev.hospital.service.MedicalTestService;
import org.oiakushev.hospital.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical_tests")
public class MedicalTestController {
    @Autowired
    private MedicalTestService medicalTestService;

    @Autowired
    private PersonalService personalService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<MedicalTestResponse> findByPersonalId(@RequestParam(name="page", defaultValue = "0") Integer page,
                                                      @RequestParam(name="size", defaultValue = "10") Integer size,
                                                      HttpServletRequest request) {
        return medicalTestService.findByPersonalId(
                personalService.auth(request, PersonalRole.Viewer).getId(),
                PageRequest.of(page, size));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MedicalTestResponse getById(@PathVariable Long id, HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Viewer);
        return medicalTestService.getById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public MedicalTestResponse add(@RequestBody MedicalTestRequest medicalTestRequest, HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Doctor);
        return medicalTestService.add(medicalTestRequest);
    }

    @RequestMapping(value = "/patients/{id}", method = RequestMethod.GET)
    public Page<MedicalTestResponse> findByPatientId(@RequestParam(name="page", defaultValue = "0") Integer page,
                                               @RequestParam(name="size", defaultValue = "10") Integer size,
                                               @PathVariable Long id,
                                               HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Viewer);
        return medicalTestService.findByPatientId(id, PageRequest.of(page, size));
    }

}
