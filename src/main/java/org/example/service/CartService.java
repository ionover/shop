package org.example.service;

import org.example.model.Cart;
import org.example.model.Product;

import java.util.HashMap;
import java.util.Map;

public class CartService {

    private final Map<String, Cart> userCarts;
    private final ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
        this.userCarts = new HashMap<>();
    }

    public Cart getOrCreateCart(String userId) {
        return userCarts.computeIfAbsent(userId, Cart::new);
    }

    public void addToCart(String userId, String productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        productService.getProductById(productId).ifPresent(product -> {
            if (product.getStockQuantity() < quantity) {
                throw new IllegalStateException("Недостаточно товара на складе");
            }
            cart.addProduct(product, quantity);
        });
    }

    public void removeFromCart(String userId, String productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        productService.getProductById(productId).ifPresent(product ->
                                                                   cart.removeProduct(product, quantity));
    }

    public void clearCart(String userId) {
        getOrCreateCart(userId).clear();
    }
}
