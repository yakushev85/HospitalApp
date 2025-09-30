package org.oiakushev.hospital.service;

import org.oiakushev.hospital.dto.ExaminationRequest;
import org.oiakushev.hospital.dto.ExaminationResponse;
import org.oiakushev.hospital.model.Examination;
import org.oiakushev.hospital.repository.ExaminationRepository;
import org.oiakushev.hospital.repository.PatientRepository;
import org.oiakushev.hospital.repository.PersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ExaminationServiceImpl implements ExaminationService {
    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PersonalRepository personalRepository;

    @Override
    public ExaminationResponse getById(Long id) {
        return ExaminationResponse.from(examinationRepository.findById(id).orElseThrow());
    }

    @Override
    public ExaminationResponse add(ExaminationRequest examinationRequest, Long personalId) {
        Examination examination = new Examination();

        examination.setDescription(examinationRequest.getDescription());
        examination.setDiagnosis(examinationRequest.getDiagnosis());

        examination.setPatient(patientRepository.findById(examinationRequest.getPatientId()).orElseThrow());
        examination.setPersonal(personalRepository.findById(personalId).orElseThrow());

        return ExaminationResponse.from(examinationRepository.save(examination));
    }

    @Override
    public Page<ExaminationResponse> findByPatientId(Long patientId, Pageable pageable) {
        return examinationRepository.findByPatientIdOrderByIdDesc(patientId, pageable).map(ExaminationResponse::from);
    }

    @Override
    public Page<ExaminationResponse> findByPersonalId(Long personalId, Pageable pageable) {
        return examinationRepository.findByPersonalIdOrderByIdDesc(personalId, pageable).map(ExaminationResponse::from);
    }
}
