package org.example.service.filter;

import org.example.model.Product;

import java.util.List;

public interface ProductFilter {

    List<Product> filter(List<Product> products);
}
