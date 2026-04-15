package com.financecoach.model;

public class Transaction {
    public String category;
    public double amount;
    public String description;

    public Transaction(String category, double amount, String description) {
        this.category = category;
        this.amount = amount;
        this.description = description;
    }
}