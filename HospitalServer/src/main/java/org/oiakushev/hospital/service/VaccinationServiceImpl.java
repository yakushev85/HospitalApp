package org.oiakushev.hospital.service;

import org.oiakushev.hospital.dto.VaccinationRequest;
import org.oiakushev.hospital.dto.VaccinationResponse;
import org.oiakushev.hospital.model.Vaccination;
import org.oiakushev.hospital.repository.PatientRepository;
import org.oiakushev.hospital.repository.PersonalRepository;
import org.oiakushev.hospital.repository.VaccinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VaccinationServiceImpl implements VaccinationService {
    @Autowired
    private VaccinationRepository vaccinationRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PersonalRepository personalRepository;

    @Override
    public VaccinationResponse getById(Long id) {
        return VaccinationResponse.from(vaccinationRepository.findById(id).orElseThrow());
    }

    @Override
    public VaccinationResponse add(VaccinationRequest vaccinationRequest) {
        Vaccination vaccination = new Vaccination();

        vaccination.setDescription(vaccinationRequest.getDescription());
        vaccination.setEffectiveTime(vaccinationRequest.getEffectiveTime());
        vaccination.setPatient(patientRepository.findById(vaccinationRequest.getPatientId()).orElseThrow());
        vaccination.setPersonal(personalRepository.findById(vaccinationRequest.getPersonalId()).orElseThrow());

        return VaccinationResponse.from(vaccinationRepository.save(vaccination));
    }

    @Override
    public Page<VaccinationResponse> findByPatientId(Long patientId, Pageable pageable) {
        return vaccinationRepository.findByPatientIdOrderByIdDesc(patientId, pageable).map(VaccinationResponse::from);
    }

    @Override
    public Page<VaccinationResponse> findByPersonalId(Long personalId, Pageable pageable) {
        return vaccinationRepository.findByPersonalIdOrderByIdDesc(personalId, pageable).map(VaccinationResponse::from);
    }
}
