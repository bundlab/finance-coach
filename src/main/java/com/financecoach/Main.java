package com.financecoach;

import java.util.Scanner;

import com.financecoach.engine.FinanceEngine;
import com.financecoach.repository.TransactionRepository;
import com.financecoach.utils.InputUtils;

public class Main {
    public static void main(String[] args) {
        try (
            InputUtils input = new InputUtils(new Scanner(System.in))
        ) {
            TransactionRepository repository = new TransactionRepository();
            FinanceEngine coach = new FinanceEngine(repository);

            System.out.println(
                "Welcome to your AI Personal Finance Coach!"
            );

            while (true) {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Add Income");
                System.out.println(
                    "2. Add Expense (Needs, Wants, Debt, Savings)"
                );
                System.out.println("3. Get AI Coaching Report");
                System.out.println("4. Exit");

                int choice = input.askInt(
                    "Choose an option: ",
                    "Please enter a number (1-4)."
                );

                if (choice == 4) {
                    break;
                }

                switch (choice) {
                    case 1 -> {
                        double amount = input.askDouble(
                            "Enter Income Amount: "
                        );

                        coach.addTransaction(
                            "Income",
                            amount,
                            "Monthly Salary"
                        );

                        System.out.println(
                            "Income added successfully!"
                        );
                    }

                    case 2 -> {
                        String category = input.askString(
                            "Category (Needs/Wants/Debt/Savings): "
                        );

                        double amount = input.askDouble(
                            "Amount: ",
                            "Please enter a valid expense amount."
                        );

                        if (amount < 0) {
                            System.out.println(
                                "Enter a valid expense amount!"
                            );
                            break;
                        }

                        coach.addTransaction(
                            category,
                            amount,
                            "User Expense"
                        );

                        System.out.println("Expense logged.");
                    }

                    case 3 -> coach.generateCoachingReport();

                    default ->
                        System.out.println(
                            "Invalid choice. Try 1-4."
                        );
                }
            }

            System.out.println(
                "Stay financially healthy! Goodbye."
            );
        }
    }
}