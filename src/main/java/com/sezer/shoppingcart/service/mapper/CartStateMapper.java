package com.sezer.shoppingcart.service.mapper;

import com.sezer.shoppingcart.domain.*;
import com.sezer.shoppingcart.service.dto.CartStateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CartState} and its DTO {@link CartStateDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CartStateMapper extends EntityMapper<CartStateDTO, CartState> {



    default CartState fromId(Long id) {
        if (id == null) {
            return null;
        }
        CartState cartState = new CartState();
        cartState.setId(id);
        return cartState;
    }
}
