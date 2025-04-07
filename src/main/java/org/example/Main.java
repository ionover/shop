package org.example;

import org.example.constants.AppConstants;
import org.example.model.Cart;
import org.example.model.MenuOption;
import org.example.model.Order;
import org.example.model.Product;
import org.example.repository.*;
import org.example.service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static ProductService productService;
    private static OrderService orderService;
    private static CartService cartService;
    private static RecommendationService recommendationService;
    private static Scanner scanner;

    public static void main(String[] args) {
        initializeServices();
        initializeSampleData();
        scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // считываем символ новой строки

            try {
                MenuOption option = MenuOption.fromValue(choice);
                switch (option) {
                    case VIEW_PRODUCTS:
                        displayProducts();
                        break;
                    case ADD_TO_CART:
                        addToCart();
                        break;
                    case VIEW_CART:
                        viewCart();
                        break;
                    case CHECKOUT:
                        checkout();
                        break;
                    case VIEW_ORDERS:
                        viewOrders();
                        break;
                    case TRACK_ORDER:
                        trackOrder();
                        break;
                    case RETURN_ORDER:
                        returnOrder();
                        break;
                    case REPEAT_ORDER:
                        repeatOrder();
                        break;
                    case RATE_PRODUCT:
                        rateProduct();
                        break;
                    case SHOW_RECOMMENDATIONS:
                        showRecommendations();
                        break;
                    case FILTER_PRODUCTS:
                        filterProducts();
                        break;
                    case EXIT:
                        System.out.println("Спасибо за покупки! До свидания!");
                        return;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void initializeServices() {
        ProductRepository productRepository = new InMemoryProductRepository();
        OrderRepository orderRepository = new InMemoryOrderRepository();

        productService = new ProductService(productRepository);
        orderService = new OrderService(orderRepository, productService);
        cartService = new CartService(productService);
        recommendationService = new RecommendationService(orderService, productService);
    }

    private static void initializeSampleData() {
        // Добавляем демонстрационные товары
        Product laptop = new Product(
                UUID.randomUUID().toString(),
                "Ноутбук Pro",
                "Высокопроизводительный ноутбук",
                new BigDecimal("99999.99"),
                "ТехКо",
                "Электроника",
                10
        );

        Product phone = new Product(
                UUID.randomUUID().toString(),
                "Смартфон X",
                "Новейший смартфон",
                new BigDecimal("59999.99"),
                "ТехКо",
                "Электроника",
                20
        );

        Product headphones = new Product(
                UUID.randomUUID().toString(),
                "Беспроводные наушники",
                "Наушники с шумоподавлением",
                new BigDecimal("19999.99"),
                "АудиоТех",
                "Электроника",
                30
        );

        ((InMemoryProductRepository) productService.getProductRepository()).save(laptop);
        ((InMemoryProductRepository) productService.getProductRepository()).save(phone);
        ((InMemoryProductRepository) productService.getProductRepository()).save(headphones);
    }

    private static void displayMenu() {
        System.out.println(String.format(AppConstants.MENU_SEPARATOR, "Интернет-магазин"));
        for (MenuOption option: MenuOption.values()) {
            System.out.printf("%d. %s%n", option.getValue(), option.getDescription());
        }
        System.out.print("Выберите действие: ");
    }

    private static void displayProducts() {
        System.out.println(String.format(AppConstants.MENU_SEPARATOR, "Доступные товары"));
        List<Product> products = productService.getAllProducts();
        products.forEach(product -> {
            System.out.printf("%s - %s (%s %s) [В наличии: %d]%n",
                              product.getId(),
                              product.getName(),
                              product.getPrice(),
                              AppConstants.CURRENCY,
                              product.getStockQuantity());
            System.out.printf("Рейтинг: %.1f/%d.0 (%d отзывов)%n",
                              product.getAverageRating(),
                              AppConstants.MAX_RATING,
                              product.getReviews().size());
            System.out.println();
        });
    }

    private static void addToCart() {
        System.out.println(String.format(AppConstants.MENU_SEPARATOR, "Добавление в корзину"));
        displayProducts();

        System.out.print("Введите ID товара: ");
        String productId = scanner.nextLine();

        System.out.print("Введите количество: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // считываем символ новой строки

        try {
            cartService.addToCart(AppConstants.DEFAULT_USER_ID, productId, quantity);
            System.out.println("Товар успешно добавлен в корзину!");
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void viewCart() {
        System.out.println(String.format(AppConstants.MENU_SEPARATOR, "Ваша корзина"));
        Cart cart = cartService.getOrCreateCart(AppConstants.DEFAULT_USER_ID);
        if (cart.getItems().isEmpty()) {
            System.out.println(AppConstants.CART_EMPTY);
            return;
        }

        cart.getItems().forEach((product, quantity) ->
                                        System.out.printf("%s - %s %s x%d = %s %s%n",
                                                          product.getName(),
                                                          product.getPrice(),
                                                          AppConstants.CURRENCY,
                                                          quantity,
                                                          product.getPrice().multiply(BigDecimal.valueOf(quantity)),
                                                          AppConstants.CURRENCY));

        System.out.printf("Итого: %s %s%n", cart.getTotalPrice(), AppConstants.CURRENCY);
    }

    private static void checkout() {
        System.out.println(String.format(AppConstants.MENU_SEPARATOR, "Оформление заказа"));
        Cart cart = cartService.getOrCreateCart(AppConstants.DEFAULT_USER_ID);

        if (cart.getItems().isEmpty()) {
            System.out.println(AppConstants.CART_EMPTY);
            return;
        }

        try {
            Order order = orderService.createOrder(cart);
            System.out.println(AppConstants.SUCCESSFUL_PURCHASE);
            System.out.println("Номер заказа: " + order.getId());
            cartService.clearCart(AppConstants.DEFAULT_USER_ID);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка при оформлении заказа: " + e.getMessage());
        }
    }

    private static void viewOrders() {
        System.out.println(String.format(AppConstants.MENU_SEPARATOR, "Ваши заказы"));
        List<Order> orders = orderService.getUserOrders(AppConstants.DEFAULT_USER_ID);

        if (orders.isEmpty()) {
            System.out.println("Заказов не найдено.");
            return;
        }

        orders.forEach(order -> {
            System.out.printf("Номер заказа: %s%n", order.getId());
            System.out.printf("Статус: %s%n", Order.getStatusInRussian(order.getStatus()));
            System.out.printf("Сумма: %s %s%n", order.getTotalPrice(), AppConstants.CURRENCY);
            System.out.println("Товары:");
            order.getItems().forEach((product, quantity) ->
                                             System.out.printf("  %s x%d%n", product.getName(), quantity));
            System.out.println();
        });
    }

    private static void trackOrder() {
        System.out.println(String.format(AppConstants.MENU_SEPARATOR, "Отслеживание заказа"));
        System.out.print("Введите номер заказа: ");
        String orderId = scanner.nextLine();

        orderService.getOrder(orderId).ifPresentOrElse(
                order -> {
                    System.out.printf("Заказ %s%n", order.getId());
                    System.out.println("История статусов:");
                    order.getTrackingHistory().forEach(event ->
                                                               System.out.println(event.toString()));
                },
                () -> System.out.println(AppConstants.ORDER_NOT_FOUND)
        );
    }

    private static void returnOrder() {
        System.out.println(String.format(AppConstants.MENU_SEPARATOR, "Возврат заказа"));
        System.out.print("Введите номер заказа: ");
        String orderId = scanner.nextLine();

        orderService.getOrder(orderId).ifPresentOrElse(
                order -> {
                    if (order.getStatus() == Order.OrderStatus.DELIVERED) {
                        System.out.print("Укажите причину возврата: ");
                        String reason = scanner.nextLine();
                        order.returnOrder(reason);
                        System.out.println("Заказ успешно возвращен.");
                    } else {
                        System.out.println("Возврат возможен только для доставленных заказов.");
                    }
                },
                () -> System.out.println(AppConstants.ORDER_NOT_FOUND)
        );
    }

    private static void repeatOrder() {
        System.out.println(String.format(AppConstants.MENU_SEPARATOR, "Повтор заказа"));
        System.out.print("Введите номер заказа для повтора: ");
        String orderId = scanner.nextLine();

        orderService.getOrder(orderId).ifPresentOrElse(
                order -> {
                    Cart cart = cartService.getOrCreateCart(AppConstants.DEFAULT_USER_ID);
                    order.getItems().forEach(cart::addProduct);
                    System.out.println("Товары из заказа добавлены в корзину.");
                    viewCart();
                },
                () -> System.out.println(AppConstants.ORDER_NOT_FOUND)
        );
    }

    private static void rateProduct() {
        System.out.println(String.format(AppConstants.MENU_SEPARATOR, "Оценка товара"));
        displayProducts();

        System.out.print("Введите ID товара: ");
        String productId = scanner.nextLine();

        productService.getProductById(productId).ifPresentOrElse(
                product -> {
                    System.out.printf("Введите оценку (%d-%d): ",
                                      AppConstants.MIN_RATING, AppConstants.MAX_RATING);
                    int rating = scanner.nextInt();
                    scanner.nextLine(); // считываем символ новой строки

                    System.out.print("Оставьте комментарий: ");
                    String comment = scanner.nextLine();

                    try {
                        product.addReview(AppConstants.DEFAULT_USER_ID, rating, comment);
                        System.out.println("Отзыв успешно добавлен!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                },
                () -> System.out.println(AppConstants.PRODUCT_NOT_FOUND)
        );
    }

    private static void filterProducts() {
        System.out.println(String.format(AppConstants.MENU_SEPARATOR, "Фильтрация товаров"));
        System.out.println("Выберите параметры фильтрации (оставьте пустым, если не хотите использовать):");

        System.out.printf("Минимальная цена (%s): ", AppConstants.CURRENCY);
        String minPriceStr = scanner.nextLine();
        BigDecimal minPrice = minPriceStr.isEmpty() ? null : new BigDecimal(minPriceStr);

        System.out.printf("Максимальная цена (%s): ", AppConstants.CURRENCY);
        String maxPriceStr = scanner.nextLine();
        BigDecimal maxPrice = maxPriceStr.isEmpty() ? null : new BigDecimal(maxPriceStr);

        System.out.print("Производитель: ");
        String manufacturer = scanner.nextLine();
        manufacturer = manufacturer.isEmpty() ? null : manufacturer;

        System.out.print("Ключевое слово для поиска: ");
        String keyword = scanner.nextLine();
        keyword = keyword.isEmpty() ? null : keyword;

        List<Product> filteredProducts = productService.filterProducts(minPrice, maxPrice, manufacturer, keyword);

        if (filteredProducts.isEmpty()) {
            System.out.println("Товары не найдены.");
            return;
        }

        System.out.println("\nНайденные товары:");
        filteredProducts.forEach(product -> {
            System.out.printf("%s - %s (%s %s) [В наличии: %d]%n",
                              product.getId(),
                              product.getName(),
                              product.getPrice(),
                              AppConstants.CURRENCY,
                              product.getStockQuantity());
            System.out.printf("Производитель: %s, Категория: %s%n",
                              product.getManufacturer(),
                              product.getCategory());
            System.out.printf("Рейтинг: %.1f/%d.0 (%d отзывов)%n",
                              product.getAverageRating(),
                              AppConstants.MAX_RATING,
                              product.getReviews().size());
            System.out.println();
        });
    }

    private static void showRecommendations() {
        System.out.println(String.format(AppConstants.MENU_SEPARATOR, "Рекомендованные товары"));
        List<Product> recommendations = recommendationService.getRecommendations(AppConstants.DEFAULT_USER_ID);

        if (recommendations.isEmpty()) {
            System.out.println("Нет рекомендаций.");
            return;
        }

        recommendations.forEach(product -> {
            System.out.printf("%s - %s %s%n",
                              product.getName(),
                              product.getPrice(),
                              AppConstants.CURRENCY);
            System.out.printf("Рейтинг: %.1f/%d.0%n",
                              product.getAverageRating(),
                              AppConstants.MAX_RATING);
            System.out.println();
        });
    }
}
