package org.example.service.filter;

import org.example.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CompositeFilter implements ProductFilter {

    private final List<ProductFilter> filters;

    public CompositeFilter() {
        this.filters = new ArrayList<>();
    }

    public void addFilter(ProductFilter filter) {
        filters.add(filter);
    }

    @Override
    public List<Product> filter(List<Product> products) {
        List<Product> result = new ArrayList<>(products);
        for (ProductFilter filter: filters) {
            result = filter.filter(result);
        }
        return result;
    }
}
