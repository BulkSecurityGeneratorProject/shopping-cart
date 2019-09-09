package com.sezer.shoppingcart.service.dto;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.sezer.shoppingcart.domain.Cart} entity.
 */
public class CartDTO implements Serializable {

    private Long id;

    private List<CartProductDTO> cartProduct;

    private Long cartStateId;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartProductDTO> getCartProduct() {
        return cartProduct;
    }

    public void setCartProduct(List<CartProductDTO> cartProduct) {
        this.cartProduct = cartProduct;
    }

    public Long getCartStateId() {
        return cartStateId;
    }

    public void setCartStateId(Long cartStateId) {
        this.cartStateId = cartStateId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CartDTO cartDTO = (CartDTO) o;
        if (cartDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cartDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CartDTO{" +
            "id=" + getId() +
            ", cartState=" + getCartStateId() +
            ", user=" + getUserId() +
            "}";
    }
}
