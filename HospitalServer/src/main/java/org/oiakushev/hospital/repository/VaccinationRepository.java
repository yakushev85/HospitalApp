package org.oiakushev.hospital.repository;

import org.oiakushev.hospital.model.Vaccination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRepository extends CrudRepository<Vaccination, Long> {
    Page<Vaccination> findByPatientIdOrderByIdDesc(Long patientId, Pageable pageable);
    Page<Vaccination> findByPersonalIdOrderByIdDesc(Long personalId, Pageable pageable);
}
