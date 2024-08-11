package com.example.project.offeser;

public class SpecialOfferUser {
    private String name;
    private String description;
    private String offerDetails;
    private String duration; // Add a field for duration
    private double totalPrice; // Add a field for total price
    private String size; // Add a field for pizza size

    public SpecialOfferUser(String name, String description, String offerDetails, String duration, double totalPrice, String size) {
        this.name = name;
        this.description = description;
        this.offerDetails = offerDetails;
        this.duration = duration; // Initialize the duration
        this.totalPrice = totalPrice; // Initialize the total price
        this.size = size; // Initialize the size
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public String getDuration() {
        return duration; // Getter for duration
    }

    public double getTotalPrice() {
        return totalPrice; // Getter for total price
    }

    public String getSize() {
        return size; // Getter for pizza size
    }
}

