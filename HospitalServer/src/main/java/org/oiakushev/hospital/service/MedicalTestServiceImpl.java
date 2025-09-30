package org.oiakushev.hospital.service;

import org.oiakushev.hospital.dto.MedicalTestRequest;
import org.oiakushev.hospital.dto.MedicalTestResponse;
import org.oiakushev.hospital.model.MedicalTest;
import org.oiakushev.hospital.repository.MedicalTestRepository;
import org.oiakushev.hospital.repository.PatientRepository;
import org.oiakushev.hospital.repository.PersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MedicalTestServiceImpl implements MedicalTestService {
    @Autowired
    private MedicalTestRepository medicalTestRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PersonalRepository personalRepository;

    @Override
    public MedicalTestResponse getById(Long id) {
        return MedicalTestResponse.from(medicalTestRepository.findById(id).orElseThrow());
    }

    @Override
    public MedicalTestResponse add(MedicalTestRequest medicalTestRequest, Long personalId) {
        MedicalTest medicalTest = new MedicalTest();

        medicalTest.setDescription(medicalTestRequest.getDescription());
        medicalTest.setResult(medicalTestRequest.getResult());
        medicalTest.setPatient(patientRepository.findById(medicalTestRequest.getPatientId()).orElseThrow());
        medicalTest.setPersonal(personalRepository.findById(personalId).orElseThrow());

        return MedicalTestResponse.from(medicalTestRepository.save(medicalTest));
    }

    @Override
    public Page<MedicalTestResponse> findByPatientId(Long patientId, Pageable pageable) {
        return medicalTestRepository.findByPatientIdOrderByIdDesc(patientId, pageable).map(MedicalTestResponse::from);
    }

    @Override
    public Page<MedicalTestResponse> findByPersonalId(Long personalId, Pageable pageable) {
        return medicalTestRepository.findByPersonalIdOrderByIdDesc(personalId, pageable).map(MedicalTestResponse::from);
    }
}
