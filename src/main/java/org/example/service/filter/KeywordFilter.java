package org.example.service.filter;

import org.example.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class KeywordFilter implements ProductFilter {

    private final String keyword;

    public KeywordFilter(String keyword) {
        this.keyword = keyword.toLowerCase();
    }

    @Override
    public List<Product> filter(List<Product> products) {
        return products.stream()
                       .filter(product ->
                                       product.getName().toLowerCase().contains(keyword) ||
                                               product.getDescription().toLowerCase().contains(keyword) ||
                                               product.getCategory().toLowerCase().contains(keyword))
                       .collect(Collectors.toList());
    }
}
