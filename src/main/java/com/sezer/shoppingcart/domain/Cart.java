package com.sezer.shoppingcart.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cart.
 */
@Entity
@Table(name = "cart")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @OneToMany(mappedBy = "cart")
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("carts")
    private CartState cartState;

    @ManyToOne
    @JsonIgnoreProperties("carts")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Cart quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Cart products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Cart addProducts(Product product) {
        this.products.add(product);
        product.setCart(this);
        return this;
    }

    public Cart removeProducts(Product product) {
        this.products.remove(product);
        product.setCart(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public CartState getCartState() {
        return cartState;
    }

    public Cart cartState(CartState cartState) {
        this.cartState = cartState;
        return this;
    }

    public void setCartState(CartState cartState) {
        this.cartState = cartState;
    }

    public User getUser() {
        return user;
    }

    public Cart user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cart)) {
            return false;
        }
        return id != null && id.equals(((Cart) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Cart{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
