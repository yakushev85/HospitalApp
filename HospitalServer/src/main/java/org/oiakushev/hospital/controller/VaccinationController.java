package org.oiakushev.hospital.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.oiakushev.hospital.dto.VaccinationRequest;
import org.oiakushev.hospital.dto.VaccinationResponse;
import org.oiakushev.hospital.model.PersonalRole;
import org.oiakushev.hospital.service.PersonalService;
import org.oiakushev.hospital.service.VaccinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vaccinations")
public class VaccinationController {
    @Autowired
    private VaccinationService vaccinationService;

    @Autowired
    private PersonalService personalService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<VaccinationResponse> findByPersonalId(@RequestParam(name="page", defaultValue = "0") Integer page,
                                                      @RequestParam(name="size", defaultValue = "10") Integer size,
                                                      HttpServletRequest request) {
        return vaccinationService.findByPersonalId(
                personalService.auth(request, PersonalRole.Viewer).getId(),
                PageRequest.of(page, size));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public VaccinationResponse getById(@PathVariable Long id, HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Viewer);
        return vaccinationService.getById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public VaccinationResponse add(@RequestBody VaccinationRequest vaccinationRequest, HttpServletRequest request) {
        return vaccinationService.add(vaccinationRequest, personalService.auth(request, PersonalRole.Nurse).getId());
    }

    @RequestMapping(value = "/patients/{id}", method = RequestMethod.GET)
    public Page<VaccinationResponse> findByPatientId(@RequestParam(name="page", defaultValue = "0") Integer page,
                                               @RequestParam(name="size", defaultValue = "10") Integer size,
                                               @PathVariable Long id,
                                               HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Viewer);
        return vaccinationService.findByPatientId(id, PageRequest.of(page, size));
    }
}
