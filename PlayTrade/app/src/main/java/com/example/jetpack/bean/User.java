package com.example.jetpack.bean;
import android.view.View;
public class User {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private View photo;//头像


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public View getPhoto() {
        return photo;
    }

    public void setPhoto(View photo) {
        this.photo = photo;
    }

    public User(String name, String email, String password, String phone, String address, View photo) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.photo = photo;
    }

    public User(){ }

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
