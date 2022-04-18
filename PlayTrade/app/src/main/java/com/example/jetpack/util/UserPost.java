package com.example.jetpack.util;

public class UserPost {
    private String PName;
    private String condition;
    private String quantity;
    private String brand;
    private String description;
    public UserPost(){}

    public UserPost(String PName, String condition, String quantity, String brand, String description) {
        this.PName = PName;
        this.condition = condition;
        this.quantity = quantity;
        this.brand = brand;
        this.description = description;
    }

    public String getPName() {
        return PName;
    }

    public void setPName(String pName) {
        this.PName = pName;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
