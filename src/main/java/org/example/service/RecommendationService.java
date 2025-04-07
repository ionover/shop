package org.example.service;

import org.example.model.Order;
import org.example.model.Product;

import java.util.*;
import java.util.stream.Collectors;

public class RecommendationService {

    private final OrderService orderService;
    private final ProductService productService;

    public RecommendationService(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    public List<Product> getRecommendations(String userId) {
        // Получаем историю заказов пользователя
        List<Order> userOrders = orderService.getUserOrders(userId);

        if (userOrders.isEmpty()) {
            // Если нет истории заказов, возвращаем топ по рейтингу
            return getTopRatedProducts(5);
        }

        // Собираем категории и производителей из истории заказов
        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, Integer> manufacturerCount = new HashMap<>();

        userOrders.forEach(order -> {
            order.getItems().keySet().forEach(product -> {
                categoryCount.merge(product.getCategory(), 1, Integer::sum);
                manufacturerCount.merge(product.getManufacturer(), 1, Integer::sum);
            });
        });

        // Находим любимую категорию и производителя
        String favoriteCategory = getMostFrequent(categoryCount);
        String favoriteManufacturer = getMostFrequent(manufacturerCount);

        // Получаем все продукты
        List<Product> allProducts = productService.getAllProducts();

        // Сортируем продукты по релевантности
        return allProducts.stream()
                          .filter(p -> !hasUserBought(p, userOrders)) // Исключаем уже купленные товары
                          .sorted((p1, p2) -> {
                              int score1 = calculateRelevanceScore(p1, favoriteCategory, favoriteManufacturer);
                              int score2 = calculateRelevanceScore(p2, favoriteCategory, favoriteManufacturer);
                              return score2 - score1; // Сортировка по убыванию
                          })
                          .limit(5)
                          .collect(Collectors.toList());
    }

    private List<Product> getTopRatedProducts(int limit) {
        return productService.getAllProducts().stream()
                             .sorted((p1, p2) -> Double.compare(p2.getAverageRating(), p1.getAverageRating()))
                             .limit(limit)
                             .collect(Collectors.toList());
    }

    private String getMostFrequent(Map<String, Integer> countMap) {
        return countMap.entrySet().stream()
                       .max(Map.Entry.comparingByValue())
                       .map(Map.Entry::getKey)
                       .orElse(null);
    }

    private boolean hasUserBought(Product product, List<Order> userOrders) {
        return userOrders.stream()
                         .anyMatch(order -> order.getItems().containsKey(product));
    }

    private int calculateRelevanceScore(Product product, String favoriteCategory, String favoriteManufacturer) {
        int score = 0;

        // Учитываем категорию
        if (product.getCategory().equals(favoriteCategory)) {
            score += 3;
        }

        // Учитываем производителя
        if (product.getManufacturer().equals(favoriteManufacturer)) {
            score += 2;
        }

        // Учитываем рейтинг
        score += Math.round(product.getAverageRating());

        return score;
    }
}
