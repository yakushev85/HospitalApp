package org.oiakushev.hospital.service;

import org.oiakushev.hospital.dto.ExaminationRequest;
import org.oiakushev.hospital.dto.ExaminationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExaminationService {
    ExaminationResponse getById(Long id);
    ExaminationResponse add(ExaminationRequest examinationRequest, Long personalId);
    Page<ExaminationResponse> findByPatientId(Long patientId, Pageable pageable);
    Page<ExaminationResponse> findByPersonalId(Long personalId, Pageable pageable);
}
