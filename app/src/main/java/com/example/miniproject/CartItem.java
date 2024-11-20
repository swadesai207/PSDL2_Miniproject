package com.example.miniproject;

public class CartItem {
    private String foodName;
    private int foodPrice;
    private int quantity;

    // Constructor
    public CartItem(String foodName, int foodPrice, int quantity) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.quantity = quantity;
    }

    // Getter for food name
    public String getFoodName() {
        return foodName;
    }

    // Setter for food name
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    // Getter for food price
    public int getFoodPrice() {
        return foodPrice;
    }

    // Setter for food price
    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    // Getter for quantity
    public int getQuantity() {
        return quantity;
    }

    // Setter for quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Method to calculate total price for the item
    public int getTotalPrice() {
        return foodPrice * quantity;
    }
}

