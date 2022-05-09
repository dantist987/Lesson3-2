package com.example.lesson3_2;

public class ContactModel {
    private String name, phone;

    public ContactModel(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public ContactModel() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
