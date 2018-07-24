package com.collector.repository;

import com.collector.domain.Licensor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Licensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LicensorRepository extends JpaRepository<Licensor, Long> {

}
