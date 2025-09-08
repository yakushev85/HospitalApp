package org.oiakushev.hospital.service;

import org.oiakushev.hospital.dto.PatientRequest;
import org.oiakushev.hospital.model.Patient;
import org.oiakushev.hospital.repository.PageablePatientRepository;
import org.oiakushev.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PageablePatientRepository pageablePatientRepository;

    @Override
    public Page<Patient> getAll(Pageable pageable) {
        return pageablePatientRepository.findAll(pageable);
    }

    @Override
    public Patient getById(Long id) {
        return patientRepository.findById(id).orElseThrow();
    }

    @Override
    public Patient add(PatientRequest patientRequest) {
        Patient newPatient = new Patient();
        newPatient.setFirstName(patientRequest.getFirstName());
        newPatient.setLastName(patientRequest.getLastName());
        newPatient.setDob(patientRequest.getDob());
        newPatient.setAddress(patientRequest.getAddress());
        newPatient.setHeight(patientRequest.getHeight());
        newPatient.setWeight(patientRequest.getWeight());
        return patientRepository.save(newPatient);
    }

    @Override
    public Patient update(Long id, PatientRequest patientRequest) {
        Patient modifyPatient = getById(id);
        modifyPatient.setFirstName(patientRequest.getFirstName());
        modifyPatient.setLastName(patientRequest.getLastName());
        modifyPatient.setDob(patientRequest.getDob());
        modifyPatient.setAddress(patientRequest.getAddress());
        modifyPatient.setHeight(patientRequest.getHeight());
        modifyPatient.setWeight(patientRequest.getWeight());
        return patientRepository.save(modifyPatient);
    }

    @Override
    public void createDummy() {
        Patient dummyParient = new Patient();
        dummyParient.setFirstName("John");
        dummyParient.setLastName("Doe");
        dummyParient.setHeight(180.0);
        dummyParient.setWeight(90.1);
        dummyParient.setAddress("New York 25/3");
        patientRepository.save(dummyParient);
    }
}
