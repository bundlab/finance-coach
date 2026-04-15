package com.financecoach.engine;

import com.financecoach.model.Transaction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class FinanceEngine {
    private List<Transaction> transactions = new ArrayList<>();
    private final String FILE_PATH = "src/main/resources/finance_data.csv";
    // Constructor: Runs as soon as the app starts
    public FinanceEngine() {
        loadFromFile();
    }

    public void addTransaction(String cat, double amt, String desc) {
    transactions.add(new Transaction(cat, amt, desc));
    
    // SAVE TO FILE
    saveToFile(cat, amt, desc);
    }

    private void saveToFile(String cat, double amt, String desc) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(cat + "," + amt + "," + desc);
        } catch (IOException e) {
            System.err.println("Coach: Error saving data: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return; // Nothing to load yet

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String cat = parts[0];
                    double amt = Double.parseDouble(parts[1]);
                    String desc = parts[2];
                    transactions.add(new Transaction(cat, amt, desc));
                }
            }
            System.out.println("Coach: History loaded. " + transactions.size() + " records found.");
        } catch (Exception e) {
            System.err.println("Coach: Error loading history: " + e.getMessage());
        }
    }

    public void generateCoachingReport() {
        // 1. Calculate totals using helper method
        double totalIncome = sumByCategory("Income");
        double needs = sumByCategory("Needs");
        double wants = sumByCategory("Wants");
        double savings = sumByCategory("Savings");
        double debt = sumByCategory("Debt");

        System.out.println("\n--- AI FINANCIAL ANALYSIS ---");
        
        if (totalIncome <= 0) {
            System.out.println("Coach: I can't analyze a $0 income. Please add income first!");
            return;
        }

        // 2. Calculate Percentages
        double needsPct = (needs / totalIncome) * 100;
        double wantsPct = (wants / totalIncome) * 100;
        double savingsPct = ((savings + debt) / totalIncome) * 100;

        System.out.printf("Total Income: $%.2f%n", totalIncome);
        System.out.printf("Budget Status: Needs: %.1f%% | Wants: %.1f%% | Savings/Debt: %.1f%%%n", 
                          needsPct, wantsPct, savingsPct);

        // 3. The "AI" Coaching Logic (Heuristics)
        System.out.println("\n--- COACH'S ADVICE ---");
        
        if (needsPct > 50) {
            System.out.println("-> [ALERT] Your 'Needs' are over 50%. Look for ways to reduce fixed costs.");
        } 
        
        if (wantsPct > 30) {
            System.out.println("-> [ADVICE] You're spending heavily on 'Wants'. Try the 48-hour rule before buying non-essentials.");
        }

        if (savingsPct < 20) {
            System.out.println("-> [STRATEGY] You're below the 20% savings goal. Try to automate a small transfer to savings today.");
        }

        if (needsPct <= 50 && wantsPct <= 30 && savingsPct >= 20) {
            System.out.println("-> [EXCELLENT] You are following the 50/30/20 rule perfectly. You're a financial pro!");
        }
    }

    // Helper method to filter and sum the amounts
    private double sumByCategory(String category) {
        return transactions.stream()
                .filter(t -> t.category.equalsIgnoreCase(category))
                .mapToDouble(t -> t.amount)
                .sum();
    }
}