package com.sezer.shoppingcart.service.mapper;

import com.sezer.shoppingcart.domain.*;
import com.sezer.shoppingcart.service.dto.CouponDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Coupon} and its DTO {@link CouponDTO}.
 */
@Mapper(componentModel = "spring", uses = {DiscountTypeMapper.class})
public interface CouponMapper extends EntityMapper<CouponDTO, Coupon> {

    @Mapping(source = "discountType.id", target = "discountTypeId")
    CouponDTO toDto(Coupon coupon);

    @Mapping(source = "discountTypeId", target = "discountType")
    Coupon toEntity(CouponDTO couponDTO);

    default Coupon fromId(Long id) {
        if (id == null) {
            return null;
        }
        Coupon coupon = new Coupon();
        coupon.setId(id);
        return coupon;
    }
}
