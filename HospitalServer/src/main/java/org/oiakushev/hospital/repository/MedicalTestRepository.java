package org.oiakushev.hospital.repository;

import org.oiakushev.hospital.model.MedicalTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalTestRepository extends CrudRepository<MedicalTest, Long> {
    Page<MedicalTest> findByPatientIdOrderByIdDesc(Long patientId, Pageable pageable);
    Page<MedicalTest> findByPersonalIdOrderByIdDesc(Long personalId, Pageable pageable);
}
