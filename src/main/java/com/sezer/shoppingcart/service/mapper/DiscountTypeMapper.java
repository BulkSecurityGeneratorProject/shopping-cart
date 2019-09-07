package com.sezer.shoppingcart.service.mapper;

import com.sezer.shoppingcart.domain.*;
import com.sezer.shoppingcart.service.dto.DiscountTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DiscountType} and its DTO {@link DiscountTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DiscountTypeMapper extends EntityMapper<DiscountTypeDTO, DiscountType> {



    default DiscountType fromId(Long id) {
        if (id == null) {
            return null;
        }
        DiscountType discountType = new DiscountType();
        discountType.setId(id);
        return discountType;
    }
}
