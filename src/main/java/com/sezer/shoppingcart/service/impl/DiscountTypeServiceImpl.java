package com.sezer.shoppingcart.service.impl;

import com.sezer.shoppingcart.service.DiscountTypeService;
import com.sezer.shoppingcart.domain.DiscountType;
import com.sezer.shoppingcart.repository.DiscountTypeRepository;
import com.sezer.shoppingcart.service.dto.DiscountTypeDTO;
import com.sezer.shoppingcart.service.mapper.DiscountTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link DiscountType}.
 */
@Service
@Transactional
public class DiscountTypeServiceImpl implements DiscountTypeService {

    private final Logger log = LoggerFactory.getLogger(DiscountTypeServiceImpl.class);

    private final DiscountTypeRepository discountTypeRepository;

    private final DiscountTypeMapper discountTypeMapper;

    public DiscountTypeServiceImpl(DiscountTypeRepository discountTypeRepository, DiscountTypeMapper discountTypeMapper) {
        this.discountTypeRepository = discountTypeRepository;
        this.discountTypeMapper = discountTypeMapper;
    }

    /**
     * Save a discountType.
     *
     * @param discountTypeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DiscountTypeDTO save(DiscountTypeDTO discountTypeDTO) {
        log.debug("Request to save DiscountType : {}", discountTypeDTO);
        DiscountType discountType = discountTypeMapper.toEntity(discountTypeDTO);
        discountType = discountTypeRepository.save(discountType);
        return discountTypeMapper.toDto(discountType);
    }

    /**
     * Get all the discountTypes.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DiscountTypeDTO> findAll() {
        log.debug("Request to get all DiscountTypes");
        return discountTypeRepository.findAll().stream()
            .map(discountTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one discountType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DiscountTypeDTO> findOne(Long id) {
        log.debug("Request to get DiscountType : {}", id);
        return discountTypeRepository.findById(id)
            .map(discountTypeMapper::toDto);
    }

    /**
     * Delete the discountType by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DiscountType : {}", id);
        discountTypeRepository.deleteById(id);
    }
}
