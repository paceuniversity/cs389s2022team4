package com.example.jetpack;

import java.io.Serializable;

public class Upload implements Serializable {
    private String PName;
    private String condition;
    private String quantity;
    private String brand;
    private String description;
    private String mImageUrl;
    private String email;

    public Upload(){
    }
    public Upload(String PName, String condition, String quantity, String brand, String description, String imageUrl, String email){
        if(PName.trim().equals("")){
            PName = "No Name";
        }
        this.PName = PName;
        this.condition = condition;
        this.quantity = quantity;
        this.brand = brand;
        this.description = description;
        mImageUrl = imageUrl;
        this.email = email;
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

    public String getImageUrl(){
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl){
        mImageUrl = imageUrl;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(){
        this.email = email;
    }

}



