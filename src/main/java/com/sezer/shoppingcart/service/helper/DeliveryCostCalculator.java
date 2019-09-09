package com.sezer.shoppingcart.service.helper;

import com.sezer.shoppingcart.service.dto.CartDTO;
import com.sezer.shoppingcart.service.dto.CartProductDTO;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeliveryCostCalculator {

    @Value("${delivery.costPerDelivery}")
    private double costPerDelivery;

    @Value("${delivery.costPerProduct}")
    private double costPerProduct;

    private double fixedCost = 2.99d;

    public double calculateFor(CartDTO cart) {
        List<CartProductDTO> products = cart.getCartProduct();
        Set<Long> distinctCategorySet = new HashSet<>();
        int numberOfProducts = products.size();
        for (CartProductDTO product : products) {
            distinctCategorySet.add(product.getProduct().getCategoryId());
        }
        int numberOfDeliveries = distinctCategorySet.size();
        return (costPerDelivery * numberOfDeliveries) + (costPerProduct*numberOfProducts);
    }
}
