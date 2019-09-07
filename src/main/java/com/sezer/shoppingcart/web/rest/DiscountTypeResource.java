package com.sezer.shoppingcart.web.rest;

import com.sezer.shoppingcart.service.DiscountTypeService;
import com.sezer.shoppingcart.web.rest.errors.BadRequestAlertException;
import com.sezer.shoppingcart.service.dto.DiscountTypeDTO;

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
 * REST controller for managing {@link com.sezer.shoppingcart.domain.DiscountType}.
 */
@RestController
@RequestMapping("/api")
public class DiscountTypeResource {

    private final Logger log = LoggerFactory.getLogger(DiscountTypeResource.class);

    private static final String ENTITY_NAME = "discountType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DiscountTypeService discountTypeService;

    public DiscountTypeResource(DiscountTypeService discountTypeService) {
        this.discountTypeService = discountTypeService;
    }

    /**
     * {@code POST  /discount-types} : Create a new discountType.
     *
     * @param discountTypeDTO the discountTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new discountTypeDTO, or with status {@code 400 (Bad Request)} if the discountType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/discount-types")
    public ResponseEntity<DiscountTypeDTO> createDiscountType(@RequestBody DiscountTypeDTO discountTypeDTO) throws URISyntaxException {
        log.debug("REST request to save DiscountType : {}", discountTypeDTO);
        if (discountTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new discountType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DiscountTypeDTO result = discountTypeService.save(discountTypeDTO);
        return ResponseEntity.created(new URI("/api/discount-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /discount-types} : Updates an existing discountType.
     *
     * @param discountTypeDTO the discountTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated discountTypeDTO,
     * or with status {@code 400 (Bad Request)} if the discountTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the discountTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/discount-types")
    public ResponseEntity<DiscountTypeDTO> updateDiscountType(@RequestBody DiscountTypeDTO discountTypeDTO) throws URISyntaxException {
        log.debug("REST request to update DiscountType : {}", discountTypeDTO);
        if (discountTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DiscountTypeDTO result = discountTypeService.save(discountTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, discountTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /discount-types} : get all the discountTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of discountTypes in body.
     */
    @GetMapping("/discount-types")
    public List<DiscountTypeDTO> getAllDiscountTypes() {
        log.debug("REST request to get all DiscountTypes");
        return discountTypeService.findAll();
    }

    /**
     * {@code GET  /discount-types/:id} : get the "id" discountType.
     *
     * @param id the id of the discountTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the discountTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/discount-types/{id}")
    public ResponseEntity<DiscountTypeDTO> getDiscountType(@PathVariable Long id) {
        log.debug("REST request to get DiscountType : {}", id);
        Optional<DiscountTypeDTO> discountTypeDTO = discountTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(discountTypeDTO);
    }

    /**
     * {@code DELETE  /discount-types/:id} : delete the "id" discountType.
     *
     * @param id the id of the discountTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/discount-types/{id}")
    public ResponseEntity<Void> deleteDiscountType(@PathVariable Long id) {
        log.debug("REST request to delete DiscountType : {}", id);
        discountTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
