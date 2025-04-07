package org.example.service.filter;

import org.example.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PriceRangeFilter implements ProductFilter {

    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;

    public PriceRangeFilter(BigDecimal minPrice, BigDecimal maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public List<Product> filter(List<Product> products) {
        return products.stream()
                       .filter(product -> {
                           BigDecimal price = product.getPrice();

                           return (minPrice == null || price.compareTo(minPrice) >= 0) &&
                                   (maxPrice == null || price.compareTo(maxPrice) <= 0);
                       })
                       .collect(Collectors.toList());
    }
}
