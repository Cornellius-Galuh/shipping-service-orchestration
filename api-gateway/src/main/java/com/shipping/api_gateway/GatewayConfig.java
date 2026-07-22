package com.shipping.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("orchestrator-service", r -> r
                        .path("/api/orders", "/api/orders/**")
                        .uri("http://localhost:8081"))
                .route("orchestrator-legacy-service", r -> r
                        .path("/api/orchestrator", "/api/orchestrator/**")
                        .filters(f -> f.rewritePath("/api/orchestrator(?<segment>.*)", "/api/orders${segment}"))
                        .uri("http://localhost:8081"))
                .route("customer-service", r -> r
                        .path("/api/customers", "/api/customers/**")
                        .uri("http://localhost:8084"))
                .route("shipment-service", r -> r
                        .path("/api/shipments", "/api/shipments/**")
                        .uri("http://localhost:8083"))
                .route("payment-service", r -> r
                        .path("/api/payments", "/api/payments/**")
                        .uri("http://localhost:8082"))
                .route("tracking-service", r -> r
                        .path("/api/tracking", "/api/tracking/**")
                        .uri("http://localhost:8086"))
                .build();
    }
}
