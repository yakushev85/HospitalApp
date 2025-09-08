package org.oiakushev.hospital.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.oiakushev.hospital.dto.BloodRequest;
import org.oiakushev.hospital.dto.BloodResponse;
import org.oiakushev.hospital.model.PersonalRole;
import org.oiakushev.hospital.service.BloodService;
import org.oiakushev.hospital.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blood")
public class BloodController {
    @Autowired
    private BloodService bloodService;

    @Autowired
    private PersonalService personalService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<BloodResponse> findByPersonalId(@RequestParam(name="page", defaultValue = "0") Integer page,
                                                @RequestParam(name="size", defaultValue = "10") Integer size,
                                                HttpServletRequest request) {
        return bloodService.findByPersonalId(
                personalService.auth(request, PersonalRole.Viewer).getId(),
                PageRequest.of(page, size));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BloodResponse getById(@PathVariable Long id, HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Viewer);
        return bloodService.getById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public BloodResponse add(@RequestBody BloodRequest bloodRequest, HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Doctor);
        return bloodService.add(bloodRequest);
    }

    @RequestMapping(value = "/patients/{id}", method = RequestMethod.GET)
    public Page<BloodResponse> findByPatientId(@RequestParam(name="page", defaultValue = "0") Integer page,
                                               @RequestParam(name="size", defaultValue = "10") Integer size,
                                               @PathVariable Long id,
                                               HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Viewer);
        return bloodService.findByPatientId(id, PageRequest.of(page, size));
    }
}
