package org.example.service;

import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.example.service.filter.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsByManufacturer(String manufacturer) {
        return productRepository.findByManufacturer(manufacturer);
    }

    public void addProductReview(String productId, String userId, int rating, String comment) {
        productRepository.findById(productId).ifPresent(product -> {
            product.addReview(userId, rating, comment);
            productRepository.update(product);
        });
    }

    public void updateStock(String productId, int quantity) {
        productRepository.findById(productId).ifPresent(product -> {
            product.setStockQuantity(quantity);
            productRepository.update(product);
        });
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public List<Product> filterProducts(BigDecimal minPrice, BigDecimal maxPrice,
                                        String manufacturer, String keyword) {
        CompositeFilter filter = new CompositeFilter();

        if (minPrice != null || maxPrice != null) {
            filter.addFilter(new PriceRangeFilter(minPrice, maxPrice));
        }

        if (manufacturer != null && !manufacturer.trim().isEmpty()) {
            filter.addFilter(new ManufacturerFilter(manufacturer));
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            filter.addFilter(new KeywordFilter(keyword));
        }

        return filter.filter(getAllProducts());
    }
}