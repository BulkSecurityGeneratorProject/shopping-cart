package com.sezer.shoppingcart.repository;

import com.sezer.shoppingcart.domain.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the CartProduct entity.
 */
@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    List<CartProduct> findAllByCart_Id(Long cardId);
}
