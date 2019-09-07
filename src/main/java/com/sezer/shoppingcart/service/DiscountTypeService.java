package com.sezer.shoppingcart.service;

import com.sezer.shoppingcart.service.dto.DiscountTypeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sezer.shoppingcart.domain.DiscountType}.
 */
public interface DiscountTypeService {

    /**
     * Save a discountType.
     *
     * @param discountTypeDTO the entity to save.
     * @return the persisted entity.
     */
    DiscountTypeDTO save(DiscountTypeDTO discountTypeDTO);

    /**
     * Get all the discountTypes.
     *
     * @return the list of entities.
     */
    List<DiscountTypeDTO> findAll();


    /**
     * Get the "id" discountType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DiscountTypeDTO> findOne(Long id);

    /**
     * Delete the "id" discountType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
