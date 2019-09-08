package com.sezer.shoppingcart.service.impl;

import com.sezer.shoppingcart.domain.Cart;
import com.sezer.shoppingcart.domain.User;
import com.sezer.shoppingcart.enums.CartStateEnum;
import com.sezer.shoppingcart.repository.CartRepository;
import com.sezer.shoppingcart.repository.UserRepository;
import com.sezer.shoppingcart.security.SecurityUtils;
import com.sezer.shoppingcart.service.CartService;
import com.sezer.shoppingcart.service.ProductService;
import com.sezer.shoppingcart.service.dto.CartDTO;
import com.sezer.shoppingcart.service.dto.ProductDTO;
import com.sezer.shoppingcart.service.mapper.CartMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Cart}.
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    private final UserRepository userRepository;

    private final ProductService productService;

    public CartServiceImpl(CartRepository cartRepository, CartMapper cartMapper, UserRepository userRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    /**
     * Save a cart.
     *
     * @param cartDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CartDTO save(CartDTO cartDTO) {
        log.debug("Request to save Cart : {}", cartDTO);
        Cart cart = cartMapper.toEntity(cartDTO);
        cart = cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    /**
     * Get all the carts.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartDTO> findAll() {
        log.debug("Request to get all Carts");
        return cartRepository.findAll().stream()
            .map(cartMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one cart by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CartDTO> findOne(Long id) {
        log.debug("Request to get Cart : {}", id);
        return cartRepository.findById(id)
            .map(cartMapper::toDto);
    }

    /**
     * Delete the cart by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cart : {}", id);
        cartRepository.deleteById(id);
    }

    public void addItem(ProductDTO product, Integer quantity) {
        // If user has cart in pending order state get this cart first else generate new Cart
        CartDTO cartDTO = cartMapper.toDto(cartRepository.findByUserIsCurrentUser(CartStateEnum.PENDING_ORDER.getId()).orElse(new Cart()));

        // If user does not have a cart firstly generate cart for him/her
        if (cartDTO.getId() == null) {
            String username = SecurityUtils.getCurrentUserLogin().orElse(null);
            User user = userRepository.findOneByLogin(username).orElse(null);
            cartDTO.setUserId(user.getId());
            cartDTO = this.save(cartDTO);
        }

        cartDTO.getProducts().add(product);
    }
}
