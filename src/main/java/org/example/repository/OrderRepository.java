package org.example.repository;

import org.example.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(String id);

    List<Order> findByUserId(String userId);

    void update(Order order);

    List<Order> findAll();
}