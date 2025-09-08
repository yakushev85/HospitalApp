package org.oiakushev.hospital.service;

import org.oiakushev.hospital.dto.BloodRequest;
import org.oiakushev.hospital.dto.BloodResponse;
import org.oiakushev.hospital.model.Blood;
import org.oiakushev.hospital.repository.BloodRepository;
import org.oiakushev.hospital.repository.PatientRepository;
import org.oiakushev.hospital.repository.PersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BloodServiceImpl implements BloodService {
    @Autowired
    private BloodRepository bloodRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PersonalRepository personalRepository;

    @Override
    public BloodResponse getById(Long id) {
        return BloodResponse.from(bloodRepository.findById(id).orElseThrow());
    }

    @Override
    public BloodResponse add(BloodRequest bloodRequest) {
        Blood newBlood = new Blood();

        newBlood.setHemoglobin(bloodRequest.getHemoglobin());
        newBlood.setHematocrit(bloodRequest.getHematocrit());
        newBlood.setMeanCorpuscularHemoglobin(bloodRequest.getMeanCorpuscularHemoglobin());
        newBlood.setMeanCorpuscularVolume(bloodRequest.getMeanCorpuscularVolume());

        newBlood.setPatient(patientRepository.findById(bloodRequest.getPatientId()).orElseThrow());
        newBlood.setPersonal(personalRepository.findById(bloodRequest.getPersonalId()).orElseThrow());

        return BloodResponse.from(bloodRepository.save(newBlood));
    }

    @Override
    public Page<BloodResponse> findByPatientId(Long patientId, Pageable pageable) {
        return bloodRepository.findByPatientIdOrderByIdDesc(patientId, pageable).map(BloodResponse::from);
    }

    @Override
    public Page<BloodResponse> findByPersonalId(Long personalId, Pageable pageable) {
        return bloodRepository.findByPersonalIdOrderByIdDesc(personalId, pageable).map(BloodResponse::from);
    }
}
