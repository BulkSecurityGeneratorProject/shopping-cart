package com.sezer.shoppingcart.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sezer.shoppingcart.domain.Coupon} entity.
 */
public class CouponDTO implements Serializable {

    private Long id;

    private String title;

    private String code;

    private Double minimumAmount;

    private Long discountTypeId;

    private DiscountTypeDTO discountType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(Double minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public Long getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(Long discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    public DiscountTypeDTO getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountTypeDTO discountType) {
        this.discountType = discountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CouponDTO couponDTO = (CouponDTO) o;
        if (couponDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), couponDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CouponDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", code='" + getCode() + "'" +
            ", minimumAmount=" + getMinimumAmount() +
            ", discountType=" + getDiscountTypeId() +
            "}";
    }
}
