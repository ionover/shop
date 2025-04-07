package org.example.repository;

import org.example.model.Order;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> orders = new HashMap<>();

    @Override
    public void save(Order order) {
        orders.put(order.getId(), order);
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order> findByUserId(String userId) {
        return orders.values().stream()
                     .filter(order -> order.getUserId().equals(userId))
                     .collect(Collectors.toList());
    }

    @Override
    public void update(Order order) {
        orders.put(order.getId(), order);
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }
}