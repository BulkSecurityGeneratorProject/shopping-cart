package com.sezer.shoppingcart.repository;

import com.sezer.shoppingcart.domain.CartState;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CartState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartStateRepository extends JpaRepository<CartState, Long> {

}
