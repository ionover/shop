package org.example.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    private final String userId;
    private final Map<Product, Integer> items;

    public Cart(String userId) {
        this.userId = userId;
        this.items = new HashMap<>();
    }

    public void addProduct(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным");
        }
        items.merge(product, quantity, Integer::sum);
    }

    public void removeProduct(Product product, int quantity) {
        if (!items.containsKey(product)) {
            throw new IllegalArgumentException("Товар не найден в корзине");
        }

        int currentQuantity = items.get(product);
        if (quantity >= currentQuantity) {
            items.remove(product);
        } else {
            items.put(product, currentQuantity - quantity);
        }
    }

    public void clear() {
        items.clear();
    }

    public BigDecimal getTotalPrice() {
        return items.entrySet().stream()
                    .map(entry -> entry.getKey().getPrice()
                                       .multiply(BigDecimal.valueOf(entry.getValue())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<Product, Integer> getItems() {
        return new HashMap<>(items);
    }

    public String getUserId() {
        return userId;
    }
}
