package org.oiakushev.hospital.service;

import org.oiakushev.hospital.dto.BloodRequest;
import org.oiakushev.hospital.dto.BloodResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BloodService {
    BloodResponse getById(Long id);
    BloodResponse add(BloodRequest bloodRequest, Long personalId);
    Page<BloodResponse> findByPatientId(Long patientId, Pageable pageable);
    Page<BloodResponse> findByPersonalId(Long personalId, Pageable pageable);
}
