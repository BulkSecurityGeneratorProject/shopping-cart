package com.sezer.shoppingcart.web.rest;

import com.sezer.shoppingcart.service.CartStateService;
import com.sezer.shoppingcart.web.rest.errors.BadRequestAlertException;
import com.sezer.shoppingcart.service.dto.CartStateDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sezer.shoppingcart.domain.CartState}.
 */
@RestController
@RequestMapping("/api")
public class CartStateResource {

    private final Logger log = LoggerFactory.getLogger(CartStateResource.class);

    private static final String ENTITY_NAME = "cartState";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CartStateService cartStateService;

    public CartStateResource(CartStateService cartStateService) {
        this.cartStateService = cartStateService;
    }

    /**
     * {@code POST  /cart-states} : Create a new cartState.
     *
     * @param cartStateDTO the cartStateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cartStateDTO, or with status {@code 400 (Bad Request)} if the cartState has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cart-states")
    public ResponseEntity<CartStateDTO> createCartState(@RequestBody CartStateDTO cartStateDTO) throws URISyntaxException {
        log.debug("REST request to save CartState : {}", cartStateDTO);
        if (cartStateDTO.getId() != null) {
            throw new BadRequestAlertException("A new cartState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CartStateDTO result = cartStateService.save(cartStateDTO);
        return ResponseEntity.created(new URI("/api/cart-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cart-states} : Updates an existing cartState.
     *
     * @param cartStateDTO the cartStateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartStateDTO,
     * or with status {@code 400 (Bad Request)} if the cartStateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cartStateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cart-states")
    public ResponseEntity<CartStateDTO> updateCartState(@RequestBody CartStateDTO cartStateDTO) throws URISyntaxException {
        log.debug("REST request to update CartState : {}", cartStateDTO);
        if (cartStateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CartStateDTO result = cartStateService.save(cartStateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cartStateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cart-states} : get all the cartStates.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cartStates in body.
     */
    @GetMapping("/cart-states")
    public List<CartStateDTO> getAllCartStates() {
        log.debug("REST request to get all CartStates");
        return cartStateService.findAll();
    }

    /**
     * {@code GET  /cart-states/:id} : get the "id" cartState.
     *
     * @param id the id of the cartStateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cartStateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cart-states/{id}")
    public ResponseEntity<CartStateDTO> getCartState(@PathVariable Long id) {
        log.debug("REST request to get CartState : {}", id);
        Optional<CartStateDTO> cartStateDTO = cartStateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cartStateDTO);
    }

    /**
     * {@code DELETE  /cart-states/:id} : delete the "id" cartState.
     *
     * @param id the id of the cartStateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cart-states/{id}")
    public ResponseEntity<Void> deleteCartState(@PathVariable Long id) {
        log.debug("REST request to delete CartState : {}", id);
        cartStateService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
