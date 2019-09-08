package com.sezer.shoppingcart.enums;

public enum DiscountTypeEnum {
    RATE(1L),
    AMOUNT(2L);

    private Long id;

    public Long getId() {
        return id;
    }

    DiscountTypeEnum(Long id) {
        this.id = id;
    }

    public static DiscountTypeEnum fromId(Long id) {
        for (DiscountTypeEnum discountTypeEnum : values()) {
            if (discountTypeEnum.getId().equals(id)) {
                return discountTypeEnum;
            }
        }
        return null;
    }
}
