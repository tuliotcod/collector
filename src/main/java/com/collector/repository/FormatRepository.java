package com.collector.repository;

import com.collector.domain.Format;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Format entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormatRepository extends JpaRepository<Format, Long> {

}
