package com.sezer.shoppingcart.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Campaign.
 */
@Entity
@Table(name = "campaign")
public class Campaign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "base_product_quantity")
    private Integer baseProductQuantity;

    @ManyToOne
    @JsonIgnoreProperties("campaigns")
    private DiscountType discountType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Campaign title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBaseProductQuantity() {
        return baseProductQuantity;
    }

    public Campaign baseProductQuantity(Integer baseProductQuantity) {
        this.baseProductQuantity = baseProductQuantity;
        return this;
    }

    public void setBaseProductQuantity(Integer baseProductQuantity) {
        this.baseProductQuantity = baseProductQuantity;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public Campaign discountType(DiscountType discountType) {
        this.discountType = discountType;
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Campaign)) {
            return false;
        }
        return id != null && id.equals(((Campaign) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Campaign{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", baseProductQuantity=" + getBaseProductQuantity() +
            "}";
    }
}
