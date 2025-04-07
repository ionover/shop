package org.example.repository;

import org.example.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAll();

    Optional<Product> findById(String id);

    List<Product> findByCategory(String category);

    List<Product> findByManufacturer(String manufacturer);

    void save(Product product);

    void update(Product product);

    void delete(String id);
}