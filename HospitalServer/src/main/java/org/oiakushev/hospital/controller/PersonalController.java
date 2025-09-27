package org.oiakushev.hospital.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.oiakushev.hospital.dto.PersonalRequest;
import org.oiakushev.hospital.dto.PersonalResponse;
import org.oiakushev.hospital.dto.SearchResultsResponse;
import org.oiakushev.hospital.model.PersonalRole;
import org.oiakushev.hospital.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal")
public class PersonalController {
    @Autowired
    private PersonalService personalService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<PersonalResponse> getAll(@RequestParam(name="page", defaultValue = "0") Integer page,
                                         @RequestParam(name="size", defaultValue = "10") Integer size,
                                         HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Admin);
        return personalService.getAll(PageRequest.of(page, size)).map(PersonalResponse::fromPersonal);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PersonalResponse getItemById(@PathVariable Long id, HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Admin);
        return PersonalResponse.fromPersonal(personalService.getById(id));
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public SearchResultsResponse<PersonalResponse> search(@RequestParam(name="text") String searchText,
                                                          HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Admin);
        return new SearchResultsResponse<>(personalService.search(searchText).stream()
                .map(PersonalResponse::fromPersonal).toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public PersonalResponse add(@RequestBody PersonalRequest personalRequest, HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Admin);
        return PersonalResponse.fromPersonal(personalService.add(personalRequest));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PersonalResponse update(@PathVariable Long id,
                           @RequestBody PersonalRequest personalRequest,
                           HttpServletRequest request) {
        personalService.auth(request, PersonalRole.Admin);
        return PersonalResponse.fromPersonal(personalService.update(id, personalRequest));
    }
}
