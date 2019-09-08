package com.sezer.shoppingcart.repository;

import com.sezer.shoppingcart.domain.Cart;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select cart from Cart cart where cart.cartState.id=:stateId and cart.user.login = ?#{principal.username}")
    Optional<Cart> findByUserIsCurrentUser(@Param("stateId") Long stateId);

}
