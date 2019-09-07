package com.sezer.shoppingcart.service.impl;

import com.sezer.shoppingcart.service.CartStateService;
import com.sezer.shoppingcart.domain.CartState;
import com.sezer.shoppingcart.repository.CartStateRepository;
import com.sezer.shoppingcart.service.dto.CartStateDTO;
import com.sezer.shoppingcart.service.mapper.CartStateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CartState}.
 */
@Service
@Transactional
public class CartStateServiceImpl implements CartStateService {

    private final Logger log = LoggerFactory.getLogger(CartStateServiceImpl.class);

    private final CartStateRepository cartStateRepository;

    private final CartStateMapper cartStateMapper;

    public CartStateServiceImpl(CartStateRepository cartStateRepository, CartStateMapper cartStateMapper) {
        this.cartStateRepository = cartStateRepository;
        this.cartStateMapper = cartStateMapper;
    }

    /**
     * Save a cartState.
     *
     * @param cartStateDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CartStateDTO save(CartStateDTO cartStateDTO) {
        log.debug("Request to save CartState : {}", cartStateDTO);
        CartState cartState = cartStateMapper.toEntity(cartStateDTO);
        cartState = cartStateRepository.save(cartState);
        return cartStateMapper.toDto(cartState);
    }

    /**
     * Get all the cartStates.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartStateDTO> findAll() {
        log.debug("Request to get all CartStates");
        return cartStateRepository.findAll().stream()
            .map(cartStateMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one cartState by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CartStateDTO> findOne(Long id) {
        log.debug("Request to get CartState : {}", id);
        return cartStateRepository.findById(id)
            .map(cartStateMapper::toDto);
    }

    /**
     * Delete the cartState by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CartState : {}", id);
        cartStateRepository.deleteById(id);
    }
}
