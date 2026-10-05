package com.financecoach.model;

import java.time.LocalDate;

public class Transaction {
    private final LocalDate date;
    private final String category;
    private final double amount;
    private final String description;

    public Transaction(String category, double amount, String description) {
        this.date = LocalDate.now();
        this.category = category;
        this.amount = amount;
        this.description = description;
    }
    public LocalDate getDate(){
        return date;
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