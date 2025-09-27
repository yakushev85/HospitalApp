package org.oiakushev.hospital.repository;

import org.oiakushev.hospital.model.Patient;
import org.oiakushev.hospital.model.Personal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalRepository extends CrudRepository<Personal, Long> {
    Personal findByUsername(String username);
    List<Personal> findByFirstNameAndLastName(String firstName, String lastName);
    List<Personal> findByPhone(String searchTxtMod);
}
