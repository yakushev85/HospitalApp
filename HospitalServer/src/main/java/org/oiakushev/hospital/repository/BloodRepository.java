package org.oiakushev.hospital.repository;

import org.oiakushev.hospital.model.Blood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodRepository extends CrudRepository<Blood, Long> {
    Page<Blood> findByPatientIdOrderByIdDesc(Long patientId, Pageable pageable);
    Page<Blood> findByPersonalIdOrderByIdDesc(Long personalId, Pageable pageable);
}
