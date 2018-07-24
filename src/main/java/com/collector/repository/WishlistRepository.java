package com.collector.repository;

import com.collector.domain.Wishlist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Wishlist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

}
