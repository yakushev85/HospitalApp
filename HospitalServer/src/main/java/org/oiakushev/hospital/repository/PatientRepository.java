package org.oiakushev.hospital.repository;

import org.oiakushev.hospital.model.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {
    List<Patient> findByFirstNameAndLastName(String firstName, String lastName);
    List<Patient> findByAddress(String address);
}
