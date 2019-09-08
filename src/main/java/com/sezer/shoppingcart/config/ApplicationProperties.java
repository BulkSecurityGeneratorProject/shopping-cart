package com.sezer.shoppingcart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Shopping Cart.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private Delivery delivery;

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public static class Delivery{
        private Double costPerDelivery;
        private Double fixedCost;

        public Double getCostPerDelivery() {
            return costPerDelivery;
        }

        public void setCostPerDelivery(Double costPerDelivery) {
            this.costPerDelivery = costPerDelivery;
        }

        public Double getFixedCost() {
            return fixedCost;
        }

        public void setFixedCost(Double fixedCost) {
            this.fixedCost = fixedCost;
        }
    }
}
