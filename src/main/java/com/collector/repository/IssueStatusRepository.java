package com.collector.repository;

import com.collector.domain.IssueStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IssueStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IssueStatusRepository extends JpaRepository<IssueStatus, Long> {

}
