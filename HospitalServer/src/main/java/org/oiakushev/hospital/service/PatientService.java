package org.oiakushev.hospital.service;

import org.oiakushev.hospital.dto.PatientRequest;
import org.oiakushev.hospital.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PatientService {
    Page<Patient> getAll(Pageable pageable);
    Patient getById(Long id);
    Patient add(PatientRequest patientRequest);
    Patient update(Long id, PatientRequest patientRequest);
    void createDummy();
    List<Patient> search(String searchText);
}
