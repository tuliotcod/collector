package com.collector.repository;

import com.collector.domain.CollectorUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CollectorUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollectorUserRepository extends JpaRepository<CollectorUser, Long> {

}
