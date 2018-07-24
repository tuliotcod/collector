package com.collector.repository;

import com.collector.domain.Finishing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Finishing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinishingRepository extends JpaRepository<Finishing, Long> {

}
