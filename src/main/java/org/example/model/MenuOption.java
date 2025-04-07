package org.example.model;

public enum MenuOption {
    EXIT(0, "Выход"),
    VIEW_PRODUCTS(1, "Просмотр товаров"),
    ADD_TO_CART(2, "Добавить в корзину"),
    VIEW_CART(3, "Просмотр корзины"),
    CHECKOUT(4, "Оформить заказ"),
    VIEW_ORDERS(5, "Просмотр заказов"),
    TRACK_ORDER(6, "Отследить заказ"),
    RETURN_ORDER(7, "Вернуть заказ"),
    REPEAT_ORDER(8, "Повторить заказ"),
    RATE_PRODUCT(9, "Оценить товар"),
    SHOW_RECOMMENDATIONS(10, "Показать рекомендации"),
    FILTER_PRODUCTS(11, "Фильтрация товаров");

    private final int value;
    private final String description;

    MenuOption(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static MenuOption fromValue(int value) {
        for (MenuOption option: MenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        throw new IllegalArgumentException("Неверный выбор меню: " + value);
    }
}