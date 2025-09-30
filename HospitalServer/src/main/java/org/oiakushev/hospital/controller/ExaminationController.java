package org.oiakushev.hospital.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.oiakushev.hospital.dto.ExaminationRequest;
import org.oiakushev.hospital.dto.ExaminationResponse;
import org.oiakushev.hospital.model.PersonalRole;
import org.oiakushev.hospital.service.ExaminationService;
import org.oiakushev.hospital.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/examinations")
public class ExaminationController {
    @Autowired
    private ExaminationService examinationService;

    @Autowired
    private PersonalService personalService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<ExaminationResponse> findByPersonalId(@RequestParam(name="page", defaultValue = "0") Integer page,
                                                      @RequestParam(name="size", defaultValue = "10") Integer size,
                                                      HttpServletRequest request) {
        return examinationService.findByPersonalId(
                personalService.auth(request, PersonalRole.Viewer).getId(),
                PageRequest.of(page, size));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ExaminationResponse getById(@PathVariable Long id, HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Viewer);
        return examinationService.getById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ExaminationResponse add(@RequestBody ExaminationRequest bloodRequest, HttpServletRequest request) {
        return examinationService.add(bloodRequest, personalService.auth(request, PersonalRole.Doctor).getId());
    }

    @RequestMapping(value = "/patients/{id}", method = RequestMethod.GET)
    public Page<ExaminationResponse> findByPatientId(@RequestParam(name="page", defaultValue = "0") Integer page,
                                               @RequestParam(name="size", defaultValue = "10") Integer size,
                                               @PathVariable Long id,
                                               HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Viewer);
        return examinationService.findByPatientId(id, PageRequest.of(page, size));
    }
}
