package org.example.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Product {

    private final String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String manufacturer;
    private String category;
    private final List<Review> reviews;
    private int stockQuantity;

    public Product(String id, String name, String description, BigDecimal price,
                   String manufacturer, String category, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.manufacturer = manufacturer;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.reviews = new ArrayList<>();
    }

    public void addReview(String userId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Рейтинг должен быть от 1 до 5");
        }
        reviews.add(new Review(userId, rating, comment));
    }

    public double getAverageRating() {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                      .mapToInt(Review::getRating)
                      .average()
                      .orElse(0.0);
    }

    public List<Review> getReviews() {
        return new ArrayList<>(reviews);
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public static class Review {

        private final String userId;
        private final int rating;
        private final String comment;
        private final long timestamp;

        public Review(String userId, int rating, String comment) {
            this.userId = userId;
            this.rating = rating;
            this.comment = comment;
            this.timestamp = System.currentTimeMillis();
        }

        public String getUserId() {
            return userId;
        }

        public int getRating() {
            return rating;
        }

        public String getComment() {
            return comment;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
