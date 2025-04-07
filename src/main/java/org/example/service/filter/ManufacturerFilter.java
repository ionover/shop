package org.example.service.filter;

import org.example.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ManufacturerFilter implements ProductFilter {

    private final String manufacturer;

    public ManufacturerFilter(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public List<Product> filter(List<Product> products) {
        return products.stream()
                       .filter(product -> product.getManufacturer().equalsIgnoreCase(manufacturer))
                       .collect(Collectors.toList());
    }
}
