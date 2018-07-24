package com.collector.repository;

import com.collector.domain.ReadingStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReadingStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReadingStatusRepository extends JpaRepository<ReadingStatus, Long> {

}
