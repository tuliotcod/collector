package com.collector.repository;

import com.collector.domain.History;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the History entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

}
