package org.oiakushev.hospital.repository;

import org.oiakushev.hospital.model.Personal;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageablePersonalRepository extends PagingAndSortingRepository<Personal, Long> {
}
