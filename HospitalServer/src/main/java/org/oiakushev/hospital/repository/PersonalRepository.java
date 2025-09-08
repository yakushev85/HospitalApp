package org.oiakushev.hospital.repository;

import org.oiakushev.hospital.model.Personal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalRepository extends CrudRepository<Personal, Long> {
    Personal findByUsername(String username);
}
