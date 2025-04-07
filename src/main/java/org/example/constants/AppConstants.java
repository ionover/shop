package org.example.constants;

public class AppConstants {

    // Рейтинг
    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;

    // ID пользователя по умолчанию (в реальном приложении было бы через аутентификацию)
    public static final String DEFAULT_USER_ID = "user1";

    // Форматирование
    public static final String CURRENCY = "руб.";
    public static final String MENU_SEPARATOR = "\n=== %s ===";

    // Сообщения
    public static final String CART_EMPTY = "Ваша корзина пуста.";
    public static final String ORDER_NOT_FOUND = "Заказ не найден.";
    public static final String PRODUCT_NOT_FOUND = "Товар не найден.";
    public static final String SUCCESSFUL_PURCHASE = "Заказ успешно оформлен!";

    private AppConstants() {
        // Приватный конструктор для предотвращения создания экземпляров
    }
}