package org.oiakushev.hospital.service;

import org.oiakushev.hospital.dto.PressureRequest;
import org.oiakushev.hospital.dto.PressureResponse;
import org.oiakushev.hospital.model.Pressure;
import org.oiakushev.hospital.repository.PatientRepository;
import org.oiakushev.hospital.repository.PersonalRepository;
import org.oiakushev.hospital.repository.PressureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PressureServiceImpl implements PressureService {
    @Autowired
    private PressureRepository pressureRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PersonalRepository personalRepository;

    @Override
    public PressureResponse getById(Long id) {
        return PressureResponse.from(pressureRepository.findById(id).orElseThrow());
    }

    @Override
    public PressureResponse add(PressureRequest pressureRequest, Long personalId) {
        Pressure pressure = new Pressure();

        pressure.setSystolic(pressureRequest.getSystolic());
        pressure.setDiastolic(pressureRequest.getDiastolic());
        pressure.setPatient(patientRepository.findById(pressureRequest.getPatientId()).orElseThrow());
        pressure.setPersonal(personalRepository.findById(personalId).orElseThrow());

        return PressureResponse.from(pressureRepository.save(pressure));
    }

    @Override
    public Page<PressureResponse> findByPatientId(Long patientId, Pageable pageable) {
        return pressureRepository.findByPatientIdOrderByIdDesc(patientId, pageable).map(PressureResponse::from);
    }

    @Override
    public Page<PressureResponse> findByPersonalId(Long personalId, Pageable pageable) {
        return pressureRepository.findByPersonalIdOrderByIdDesc(personalId, pageable).map(PressureResponse::from);
    }
}
