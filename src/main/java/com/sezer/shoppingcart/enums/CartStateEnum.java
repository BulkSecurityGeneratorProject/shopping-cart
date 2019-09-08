package com.sezer.shoppingcart.enums;

public enum CartStateEnum {
    PENDING_ORDER(1L, "Pending Order"),
    ORDERED(2L, "Ordered"),
    RECEIVED(3L, "Received");

    private Long id;
    private String title;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static CartStateEnum fromId(Long id) {
        for (CartStateEnum cartStateEnum : values()) {
            if (cartStateEnum.getId().equals(id)) {
                return cartStateEnum;
            }
        }
        return null;
    }

    CartStateEnum(Long id, String title){
        this.id = id;
        this.title = title;
    }
}
