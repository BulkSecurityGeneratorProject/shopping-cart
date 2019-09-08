package com.sezer.shoppingcart.service;

import com.sezer.shoppingcart.service.dto.CartProductDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sezer.shoppingcart.domain.CartProduct}.
 */
public interface CartProductService {

    /**
     * Save a cartProduct.
     *
     * @param cartProductDTO the entity to save.
     * @return the persisted entity.
     */
    CartProductDTO save(CartProductDTO cartProductDTO);

    /**
     * Get all the cartProducts.
     *
     * @return the list of entities.
     */
    List<CartProductDTO> findAll();


    /**
     * Get the "id" cartProduct.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CartProductDTO> findOne(Long id);

    /**
     * Delete the "id" cartProduct.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<CartProductDTO> findAllByCartId(Long cartId);
}
