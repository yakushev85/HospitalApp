package org.oiakushev.hospital.repository;

import org.oiakushev.hospital.model.Patient;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageablePatientRepository extends PagingAndSortingRepository<Patient, Long> {
}
