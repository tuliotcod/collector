package com.collector.repository;

import com.collector.domain.IssueInWishlist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IssueInWishlist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IssueInWishlistRepository extends JpaRepository<IssueInWishlist, Long> {

}
