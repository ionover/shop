package org.example.repository;

import org.example.model.Product;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> products = new HashMap<>();

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findByCategory(String category) {
        return products.values().stream()
                       .filter(p -> p.getCategory().equals(category))
                       .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByManufacturer(String manufacturer) {
        return products.values().stream()
                       .filter(p -> p.getManufacturer().equals(manufacturer))
                       .collect(Collectors.toList());
    }

    @Override
    public void save(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public void update(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public void delete(String id) {
        products.remove(id);
    }
}