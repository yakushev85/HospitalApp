package org.oiakushev.hospital.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.oiakushev.hospital.dto.PressureRequest;
import org.oiakushev.hospital.dto.PressureResponse;
import org.oiakushev.hospital.model.PersonalRole;
import org.oiakushev.hospital.service.PersonalService;
import org.oiakushev.hospital.service.PressureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pressure")
public class PressureController {
    @Autowired
    private PressureService pressureService;

    @Autowired
    private PersonalService personalService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<PressureResponse> findByPersonalId(@RequestParam(name="page", defaultValue = "0") Integer page,
                                                   @RequestParam(name="size", defaultValue = "10") Integer size,
                                                   HttpServletRequest request) {
        return pressureService.findByPersonalId(
                personalService.auth(request, PersonalRole.Viewer).getId(),
                PageRequest.of(page, size));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PressureResponse getById(@PathVariable Long id, HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Viewer);
        return pressureService.getById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public PressureResponse add(@RequestBody PressureRequest pressureRequest, HttpServletRequest request) {
        return pressureService.add(pressureRequest, personalService.auth(request, PersonalRole.Nurse).getId());
    }

    @RequestMapping(value = "/patients/{id}", method = RequestMethod.GET)
    public Page<PressureResponse> findByPatientId(@RequestParam(name="page", defaultValue = "0") Integer page,
                                               @RequestParam(name="size", defaultValue = "10") Integer size,
                                               @PathVariable Long id,
                                               HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Viewer);
        return pressureService.findByPatientId(id, PageRequest.of(page, size));
    }
}
