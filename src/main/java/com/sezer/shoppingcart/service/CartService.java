package com.sezer.shoppingcart.service;

import com.sezer.shoppingcart.service.dto.CartDTO;
import com.sezer.shoppingcart.service.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sezer.shoppingcart.domain.Cart}.
 */
public interface CartService {

    /**
     * Save a cart.
     *
     * @param cartDTO the entity to save.
     * @return the persisted entity.
     */
    CartDTO save(CartDTO cartDTO);

    /**
     * Get all the carts.
     *
     * @return the list of entities.
     */
    List<CartDTO> findAll();


    /**
     * Get the "id" cart.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CartDTO> findOne(Long id);

    /**
     * Delete the "id" cart.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void addItem(ProductDTO product);

    double getTotalAmountAfterDiscounts(String campaignCode);

    double getCampaignDiscount(CartDTO cartDTO);

    double getCouponDiscount(String code, Double subTotal);

    double getDeliveryCost(CartDTO cartDTO);
}
