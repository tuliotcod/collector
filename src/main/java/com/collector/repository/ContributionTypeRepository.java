package com.collector.repository;

import com.collector.domain.ContributionType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ContributionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContributionTypeRepository extends JpaRepository<ContributionType, Long> {

}
