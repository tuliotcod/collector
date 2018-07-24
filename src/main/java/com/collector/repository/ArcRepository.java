package com.collector.repository;

import com.collector.domain.Arc;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Arc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArcRepository extends JpaRepository<Arc, Long> {

}
