package com.example.bookworm;

public class Product {
    private String title;
    private String description;
    private String imgUrl;
    private double price;

    // Default constructor (no-argument constructor) required for Firebase deserialization
    public Product() {
        // Default constructor required by Firebase Realtime Database
    }

    public Product(String title, String description, String imgUrl, double price) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
