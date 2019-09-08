package com.sezer.shoppingcart.service.impl;

import com.sezer.shoppingcart.domain.CartProduct;
import com.sezer.shoppingcart.repository.CartProductRepository;
import com.sezer.shoppingcart.service.CartProductService;
import com.sezer.shoppingcart.service.dto.CartProductDTO;
import com.sezer.shoppingcart.service.mapper.CartProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CartProduct}.
 */
@Service
@Transactional
public class CartProductServiceImpl implements CartProductService {

    private final Logger log = LoggerFactory.getLogger(CartProductServiceImpl.class);

    private final CartProductRepository cartProductRepository;

    private final CartProductMapper cartProductMapper;

    public CartProductServiceImpl(CartProductRepository cartProductRepository, CartProductMapper cartProductMapper) {
        this.cartProductRepository = cartProductRepository;
        this.cartProductMapper = cartProductMapper;
    }

    /**
     * Save a cartProduct.
     *
     * @param cartProductDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CartProductDTO save(CartProductDTO cartProductDTO) {
        log.debug("Request to save CartProduct : {}", cartProductDTO);
        CartProduct cartProduct = cartProductMapper.toEntity(cartProductDTO);
        cartProduct = cartProductRepository.save(cartProduct);
        return cartProductMapper.toDto(cartProduct);
    }

    /**
     * Get all the cartProducts.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartProductDTO> findAll() {
        log.debug("Request to get all CartProducts");
        return cartProductRepository.findAll().stream()
            .map(cartProductMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one cartProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CartProductDTO> findOne(Long id) {
        log.debug("Request to get CartProduct : {}", id);
        return cartProductRepository.findById(id)
            .map(cartProductMapper::toDto);
    }

    /**
     * Delete the cartProduct by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CartProduct : {}", id);
        cartProductRepository.deleteById(id);
    }

    @Override
    public List<CartProductDTO> findAllByCartId(Long cartId) {
        log.debug("Request to get CartProducts with cartId: {}", cartId);
        return cartProductRepository.findAllByCart_Id(cartId).stream()
            .map(cartProductMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
