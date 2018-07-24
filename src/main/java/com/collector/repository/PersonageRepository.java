package com.collector.repository;

import com.collector.domain.Personage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Personage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonageRepository extends JpaRepository<Personage, Long> {

}
