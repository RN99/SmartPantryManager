package com.example.smartpantrymanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ingredient {
    private int id;
    private String name;
    private double quantity;
    private String unit;
    private String expiryDate;

    //  Constructor to match MainActivity
    public Ingredient(int id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getExpiryDate() { return expiryDate; }

    // method to check expiry
    public boolean isExpired() {
        if (expiryDate == null || expiryDate.isEmpty()) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date expiry = sdf.parse(expiryDate);
            Date today = new Date();
            return expiry.before(today);
        } catch (ParseException e) {
            return false;
        }
    }

    // Check if expiring soon (within 3 days)
    public boolean isExpiringSoon() {
        if (expiryDate == null || expiryDate.isEmpty()) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date expiry = sdf.parse(expiryDate);
            Date today = new Date();
            long diff = expiry.getTime() - today.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            return days >= 0 && days <= 3;
        } catch (ParseException e) {
            return false;
        }
    }
}
