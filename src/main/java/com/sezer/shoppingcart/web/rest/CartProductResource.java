package com.sezer.shoppingcart.web.rest;

import com.sezer.shoppingcart.service.CartProductService;
import com.sezer.shoppingcart.web.rest.errors.BadRequestAlertException;
import com.sezer.shoppingcart.service.dto.CartProductDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sezer.shoppingcart.domain.CartProduct}.
 */
@RestController
@RequestMapping("/api")
public class CartProductResource {

    private final Logger log = LoggerFactory.getLogger(CartProductResource.class);

    private static final String ENTITY_NAME = "cartProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CartProductService cartProductService;

    public CartProductResource(CartProductService cartProductService) {
        this.cartProductService = cartProductService;
    }

    /**
     * {@code POST  /cart-products} : Create a new cartProduct.
     *
     * @param cartProductDTO the cartProductDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cartProductDTO, or with status {@code 400 (Bad Request)} if the cartProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cart-products")
    public ResponseEntity<CartProductDTO> createCartProduct(@Valid @RequestBody CartProductDTO cartProductDTO) throws URISyntaxException {
        log.debug("REST request to save CartProduct : {}", cartProductDTO);
        if (cartProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new cartProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CartProductDTO result = cartProductService.save(cartProductDTO);
        return ResponseEntity.created(new URI("/api/cart-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cart-products} : Updates an existing cartProduct.
     *
     * @param cartProductDTO the cartProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartProductDTO,
     * or with status {@code 400 (Bad Request)} if the cartProductDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cartProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cart-products")
    public ResponseEntity<CartProductDTO> updateCartProduct(@Valid @RequestBody CartProductDTO cartProductDTO) throws URISyntaxException {
        log.debug("REST request to update CartProduct : {}", cartProductDTO);
        if (cartProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CartProductDTO result = cartProductService.save(cartProductDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cartProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cart-products} : get all the cartProducts.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cartProducts in body.
     */
    @GetMapping("/cart-products")
    public List<CartProductDTO> getAllCartProducts() {
        log.debug("REST request to get all CartProducts");
        return cartProductService.findAll();
    }

    /**
     * {@code GET  /cart-products/:id} : get the "id" cartProduct.
     *
     * @param id the id of the cartProductDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cartProductDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cart-products/{id}")
    public ResponseEntity<CartProductDTO> getCartProduct(@PathVariable Long id) {
        log.debug("REST request to get CartProduct : {}", id);
        Optional<CartProductDTO> cartProductDTO = cartProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cartProductDTO);
    }

    /**
     * {@code DELETE  /cart-products/:id} : delete the "id" cartProduct.
     *
     * @param id the id of the cartProductDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cart-products/{id}")
    public ResponseEntity<Void> deleteCartProduct(@PathVariable Long id) {
        log.debug("REST request to delete CartProduct : {}", id);
        cartProductService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
