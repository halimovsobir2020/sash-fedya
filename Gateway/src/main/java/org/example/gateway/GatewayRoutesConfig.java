package org.example.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("order-service", r -> r.path("/api/order/**")
                        .filters(f -> f.rewritePath("/api/order/(?<segment>.*)", "/${segment}"))
                        .uri("lb://ORDERSERVICE"))

                .route("product-service", r -> r.path("/api/product/**")
                        .filters(f -> f.rewritePath("/api/product/(?<segment>.*)", "/${segment}"))
                        .uri("lb://PRODUCTSERVICE"))

                .route("inventory-service", r -> r.path("/api/inventory/**")
                        .filters(f -> f.rewritePath("/api/inventory/(?<segment>.*)", "/${segment}"))
                        .uri("lb://INVENTORYSERVICE"))

                .route("payment-service", r -> r.path("/api/payment/**")
                        .filters(f -> f.rewritePath("/api/payment(?<segment>/.*)?", "/${segment}"))
                        .uri("lb://PAYMENTSERVICE"))
                .build();
    }

}
