package org.oiakushev.hospital.repository;

import org.oiakushev.hospital.model.Examination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationRepository extends CrudRepository<Examination, Long> {
    Page<Examination> findByPatientIdOrderByIdDesc(Long patientId, Pageable pageable);
    Page<Examination> findByPersonalIdOrderByIdDesc(Long personalId, Pageable pageable);
}
