package com.collector.repository;

import com.collector.domain.IssueInCollection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IssueInCollection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IssueInCollectionRepository extends JpaRepository<IssueInCollection, Long> {

}
