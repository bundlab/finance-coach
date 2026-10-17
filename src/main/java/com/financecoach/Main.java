package com.financecoach;

import java.util.Scanner;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import com.financecoach.engine.FinanceEngine;
import com.financecoach.model.MonthlyReport;
import com.financecoach.repository.TransactionRepository;
import com.financecoach.service.ExportService;
import com.financecoach.utils.InputUtils;
import java.time.LocalDate;
import java.util.List;

import com.financecoach.model.Transaction;

public class Main {

    public static void main(String[] args) {

        try (
            InputUtils input = new InputUtils(new Scanner(System.in))
        ) {

            TransactionRepository repository =
                new TransactionRepository();

            FinanceEngine coach =
                new FinanceEngine(repository);

            ExportService exporter =
                new ExportService();

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
                System.out.println("4. View/Export Monthly Text Report");
                System.out.println("5. Export Transactions CSV");
                System.out.println("6. View Transaction History");
                System.out.println("7. Exit");

                int choice = input.askInt(
                    "Choose an option: ",
                    "Please enter a number (1-7)."
                );

                if (choice == 7) {
                    break;
                }

                switch (choice) {

                    case 1 -> {

                        double amount = input.askDouble(
                            "Enter Income Amount: "
                        );

                        if (amount < 0) {
                            System.out.println(
                                "Please enter a valid income amount."
                            );
                            break;
                        }

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

                        System.out.println(
                            "Expense logged."
                        );
                    }

                    case 3 -> {

                        coach.generateCoachingReport();
                    }

                    case 4 -> {
                        YearMonth month = askReportMonth(input);
                        MonthlyReport report = coach.generateMonthlyReport(month);
                        String reportText = exporter.renderMonthlyReport(report);
                        System.out.print(reportText);

                        String saveReport = input.askString(
                            "Save this report as a text file? (y/n): "
                        );
                        if (saveReport.equalsIgnoreCase("y")) {
                            try {
                                System.out.println(
                                    "Report saved to "
                                        + exporter.exportMonthlyReport(report)
                                );
                            } catch (java.io.IOException e) {
                                System.err.println(
                                    "Coach: Error saving report: "
                                        + e.getMessage()
                                );
                            }
                        }
                    }

                    case 5 -> {

                        exporter.exportMonthlySummary(
                            coach.getTransactions()
                        );
                    }

                    case 6-> viewTransactionHistory(input,coach);

                    default -> {

                        System.out.println(
                            "Invalid choice. Try 1-5."
                        );
                    }
                }
            }

            System.out.println(
                "Stay financially healthy! Goodbye."
            );
        }
    }


    private static void viewTransactionHistory(
            InputUtils input,
            FinanceEngine coach
    ) {
        System.out.println("\n--- TRANSACTION HISTORY ---");
        System.out.println(
                "Categories: Income, Needs, Wants, Savings, Debt"
        );

        String category = input.askString(
                "Category (leave blank for all): "
        );

        if (!category.isBlank()
                && !List.of(
                "Income", "Needs", "Wants", "Savings", "Debt"
        ).stream().anyMatch(c -> c.equalsIgnoreCase(category))) {
            System.out.println("Invalid category. Returning to menu.");
            return;
        }

        LocalDate startDate = askOptionalDate(
                input, "Start date (YYYY-MM-DD, blank for none): "
        );
        LocalDate endDate = askOptionalDate(
                input, "End date (YYYY-MM-DD, blank for none): "
        );

        if (startDate != null && endDate != null
                && startDate.isAfter(endDate)) {
            System.out.println(
                    "Start date cannot be after the end date."
            );
            return;
        }

        List<Transaction> history = coach.getTransactionHistory(
                category, startDate, endDate
        );

        if (history.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.printf(
                "%-12s %-12s %12s  %s%n",
                "Date", "Category", "Amount", "Description"
        );
        System.out.println("-".repeat(75));

        for (Transaction transaction : history) {
            String date = transaction.getDate() == null
                    ? "Unknown"
                    : transaction.getDate().toString();

            System.out.printf(
                    "%-12s %-12s %12.2f  %s%n",
                    date,
                    transaction.getCategory(),
                    transaction.getAmount(),
                    transaction.getDescription()
            );
        }

        System.out.println("\nTotal transactions: " + history.size());
    }

    private static LocalDate askOptionalDate(
            InputUtils input,
            String prompt
    ) {
        while (true) {
            String value = input.askString(prompt);

            if (value.isBlank()) {
                return null;
            }

            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException e) {
                System.out.println(
                        "Invalid date. Use YYYY-MM-DD, or leave blank."
                );
            }
        }
    }


    private static YearMonth askReportMonth(InputUtils input) {
        while (true) {
            String value = input.askString("Report month (YYYY-MM): ");
            try {
                YearMonth month = YearMonth.parse(value);
                if (month.toString().equals(value)) {
                    return month;
                }
            } catch (DateTimeParseException ignored) {
            }
            System.out.println("Enter a valid month in YYYY-MM format.");
        }
    }
}