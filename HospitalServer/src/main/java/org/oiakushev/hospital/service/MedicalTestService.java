package org.oiakushev.hospital.service;

import org.oiakushev.hospital.dto.MedicalTestRequest;
import org.oiakushev.hospital.dto.MedicalTestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicalTestService {
    MedicalTestResponse getById(Long id);
    MedicalTestResponse add(MedicalTestRequest medicalTestRequest);
    Page<MedicalTestResponse> findByPatientId(Long patientId, Pageable pageable);
    Page<MedicalTestResponse> findByPersonalId(Long personalId, Pageable pageable);
}
