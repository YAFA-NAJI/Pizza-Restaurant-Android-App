package com.example.project.AllOrders;

import java.util.List;

public class Order {
    private String pizzaName;
    private String size;
    private String quantity;
    private String unitPrice;
    private String totalPrice;
    private String dateTime;
    private String customerName;

    public Order(String pizzaName, String size, String quantity, String unitPrice, String totalPrice, String dateTime, String customerName) {
        this.pizzaName = pizzaName;
        this.size = size;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
        this.customerName = customerName;
    }

    // Getters
    public String getPizzaName() { return pizzaName; }
    public String getSize() { return size; }
    public String getQuantity() { return quantity; }
    public String getUnitPrice() { return unitPrice; }
    public String getTotalPrice() { return totalPrice; }
    public String getDateTime() { return dateTime; }
    public String getCustomerName() { return customerName; }


}
