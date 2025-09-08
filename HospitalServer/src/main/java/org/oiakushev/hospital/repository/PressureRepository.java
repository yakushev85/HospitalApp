package org.oiakushev.hospital.repository;

import org.oiakushev.hospital.model.Pressure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PressureRepository extends CrudRepository<Pressure, Long> {
    Page<Pressure> findByPatientIdOrderByIdDesc(Long patientId, Pageable pageable);
    Page<Pressure> findByPersonalIdOrderByIdDesc(Long personalId, Pageable pageable);
}
