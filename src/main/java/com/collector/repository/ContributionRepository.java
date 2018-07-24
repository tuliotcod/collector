package com.collector.repository;

import com.collector.domain.Contribution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Contribution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {

}
