package org.example.service;

import org.example.model.Cart;
import org.example.model.Order;
import org.example.repository.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public Order createOrder(Cart cart) {
        // Проверяем наличие товаров
        cart.getItems().forEach((product, quantity) -> {
            if (product.getStockQuantity() < quantity) {
                throw new IllegalStateException("Недостаточно товара на складе: " + product.getName());
            }
        });

        // Обновляем количество товаров
        cart.getItems().forEach((product, quantity) ->
                                        productService.updateStock(product.getId(),
                                                                   product.getStockQuantity() - quantity));

        // Создаем и сохраняем заказ
        Order order = new Order(
                UUID.randomUUID().toString(),
                cart.getUserId(),
                cart.getItems(),
                cart.getTotalPrice()
        );

        orderRepository.save(order);
        return order;
    }

    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public void updateOrderStatus(String orderId, Order.OrderStatus status) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.update(order);
        });
    }
}
