package com.sezer.shoppingcart.service;

import com.sezer.shoppingcart.service.dto.CartStateDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sezer.shoppingcart.domain.CartState}.
 */
public interface CartStateService {

    /**
     * Save a cartState.
     *
     * @param cartStateDTO the entity to save.
     * @return the persisted entity.
     */
    CartStateDTO save(CartStateDTO cartStateDTO);

    /**
     * Get all the cartStates.
     *
     * @return the list of entities.
     */
    List<CartStateDTO> findAll();


    /**
     * Get the "id" cartState.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CartStateDTO> findOne(Long id);

    /**
     * Delete the "id" cartState.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
