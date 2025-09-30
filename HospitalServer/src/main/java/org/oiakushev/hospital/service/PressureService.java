package org.oiakushev.hospital.service;

import org.oiakushev.hospital.dto.PressureRequest;
import org.oiakushev.hospital.dto.PressureResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PressureService {
    PressureResponse getById(Long id);
    PressureResponse add(PressureRequest pressureRequest, Long personalId);
    Page<PressureResponse> findByPatientId(Long patientId, Pageable pageable);
    Page<PressureResponse> findByPersonalId(Long personalId, Pageable pageable);
}
