package com.financecoach.model;

public class Transaction {
    private final String category;
    private final double amount;
    private final String description;

    public Transaction(String category, double amount, String description) {
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}