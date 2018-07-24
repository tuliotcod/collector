package com.collector.repository;

import com.collector.domain.Collaborator;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Collaborator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

}
