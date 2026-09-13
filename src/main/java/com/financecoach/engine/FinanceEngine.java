package com.financecoach.engine;

import java.util.ArrayList;
import java.util.List;

import com.financecoach.model.Transaction;
import com.financecoach.repository.TransactionRepository;

public class FinanceEngine {
    private final List<Transaction> transactions = new ArrayList<>();
    private final TransactionRepository repository;

    public FinanceEngine(TransactionRepository repository) {
        this.repository = repository;
        transactions.addAll(repository.load());

        System.out.println(
            "Coach: History loaded. "
                + transactions.size()
                + " records found."
        );
    }

    public void addTransaction(
        String category,
        double amount,
        String description
    ) {
        Transaction transaction =
            new Transaction(category, amount, description);

        transactions.add(transaction);
        repository.save(transaction);
    }

    public void generateCoachingReport() {
        // * 1. Calculate totals using helper method
        double totalIncome = sumByCategory("Income");
        double needs = sumByCategory("Needs");
        double wants = sumByCategory("Wants");
        double savings = sumByCategory("Savings");
        double debt = sumByCategory("Debt");

        System.out.println("\n--- AI FINANCIAL ANALYSIS ---");

        if (totalIncome <= 0) {
            System.out.println(
                "Coach: I can't analyze a $0 income. "
                + "Please add income first!"
            );
            return;
        }

        // * 2. Calculate percentages
        double needsPct = (needs / totalIncome) * 100;
        double wantsPct = (wants / totalIncome) * 100;
        double savingsPct = ((savings + debt) / totalIncome) * 100;

        System.out.printf("Total Income: $%.2f%n", totalIncome);
        System.out.printf(
            "Budget Status: Needs: %.1f%% | Wants: %.1f%% | "
            + "Savings/Debt: %.1f%%%n",
            needsPct,
            wantsPct,
            savingsPct
        );

        // * 3. The "AI" Coaching Logic (Heuristics)
        System.out.println("\n--- COACH'S ADVICE ---");

        if (needsPct > 50) {
            System.out.println(
                "-> [ALERT] Your 'Needs' are over 50%. "
                + "Look for ways to reduce fixed costs."
            );
        }

        if (wantsPct > 30) {
            System.out.println(
                "-> [ADVICE] You're spending heavily on 'Wants'. "
                + "Try the 48-hour rule before buying non-essentials."
            );
        }

        if (savingsPct < 20) {
            System.out.println(
                "-> [STRATEGY] You're below the 20% savings goal. "
                + "Try to automate a small transfer to savings today."
            );
        }

        if (needsPct <= 50 && wantsPct <= 30 && savingsPct >= 20) {
            System.out.println(
                "-> [EXCELLENT] You are following the 50/30/20 rule "
                + "perfectly. You're a financial pro!"
            );
        }
    }

    private double sumByCategory(String category) {
        return transactions.stream()
            .filter(
                transaction ->
                    transaction.getCategory().equalsIgnoreCase(category)
            )
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
}