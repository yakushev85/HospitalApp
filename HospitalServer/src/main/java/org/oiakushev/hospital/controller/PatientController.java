package org.oiakushev.hospital.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.oiakushev.hospital.dto.PatientRequest;
import org.oiakushev.hospital.model.Patient;
import org.oiakushev.hospital.model.PersonalRole;
import org.oiakushev.hospital.service.PatientService;
import org.oiakushev.hospital.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private PersonalService personalService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<Patient> getAll(@RequestParam(name="page", defaultValue = "0") Integer page,
                                @RequestParam(name="size", defaultValue = "10") Integer size,
                                HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Viewer);
        return patientService.getAll(PageRequest.of(page, size));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Patient getItemById(@PathVariable Long id, HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Viewer);
        return patientService.getById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Patient add(@RequestBody PatientRequest patientRequest, HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Nurse);
        return patientService.add(patientRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Patient update(@PathVariable Long id,
                          @RequestBody PatientRequest patientRequest,
                          HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Doctor);
        return patientService.update(id, patientRequest);
    }
}
