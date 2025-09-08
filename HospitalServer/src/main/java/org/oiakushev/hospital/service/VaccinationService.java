package org.oiakushev.hospital.service;

import org.oiakushev.hospital.dto.VaccinationRequest;
import org.oiakushev.hospital.dto.VaccinationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VaccinationService {
    VaccinationResponse getById(Long id);
    VaccinationResponse add(VaccinationRequest vaccinationRequest);
    Page<VaccinationResponse> findByPatientId(Long patientId, Pageable pageable);
    Page<VaccinationResponse> findByPersonalId(Long personalId, Pageable pageable);
}
