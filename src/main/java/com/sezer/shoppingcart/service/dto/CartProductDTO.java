package com.sezer.shoppingcart.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sezer.shoppingcart.domain.CartProduct} entity.
 */
public class CartProductDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer quantity;

    private ProductDTO product;

    private Long cartId;

    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CartProductDTO cartProductDTO = (CartProductDTO) o;
        if (cartProductDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cartProductDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CartProductDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", cart=" + getCartId() +
            ", product=" + getProductId() +
            "}";
    }
}
