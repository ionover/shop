package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Order {

    private final String id;
    private final String userId;
    private final Map<Product, Integer> items;
    private final BigDecimal totalPrice;
    private final LocalDateTime orderDate;
    private OrderStatus status;
    private final List<TrackingEvent> trackingHistory;
    private String returnReason;
    private LocalDateTime returnDate;

    public Order(String id, String userId, Map<Product, Integer> items, BigDecimal totalPrice) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
        this.trackingHistory = new ArrayList<>();
        addTrackingEvent("Заказ создан");
    }

    public void addTrackingEvent(String description) {
        trackingHistory.add(new TrackingEvent(description));
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Map<Product, Integer> getItems() {
        return items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<TrackingEvent> getTrackingHistory() {
        return new ArrayList<>(trackingHistory);
    }

    public String getReturnReason() {
        return returnReason;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        addTrackingEvent("Статус изменен на: " + getStatusInRussian(status));
    }

    public void returnOrder(String reason) {
        this.returnReason = reason;
        this.returnDate = LocalDateTime.now();
        setStatus(OrderStatus.RETURNED);
        addTrackingEvent("Заказ возвращен. Причина: " + reason);
    }

    public static String getStatusInRussian(OrderStatus status) {
        switch (status) {
            case PENDING:
                return "Ожидает обработки";
            case CONFIRMED:
                return "Подтвержден";
            case SHIPPED:
                return "Отправлен";
            case DELIVERED:
                return "Доставлен";
            case CANCELLED:
                return "Отменен";
            case RETURNED:
                return "Возвращен";
            default:
                return status.toString();
        }
    }

    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        SHIPPED,
        DELIVERED,
        CANCELLED,
        RETURNED
    }

    public static class TrackingEvent {

        private final LocalDateTime timestamp;
        private final String description;

        public TrackingEvent(String description) {
            this.timestamp = LocalDateTime.now();
            this.description = description;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return String.format("%s: %s",
                                 timestamp.toString().replace("T", " "),
                                 description);
        }
    }
}
