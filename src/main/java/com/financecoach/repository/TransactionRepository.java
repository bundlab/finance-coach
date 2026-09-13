package com.financecoach.repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.financecoach.model.Transaction;

public class TransactionRepository {
    private static final String FILE_PATH =
        "src/main/resources/finance_data.csv";

    public List<Transaction> load() {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return transactions;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length != 3) {
                    continue;
                }

                String category = parts[0];
                double amount = Double.parseDouble(parts[1]);
                String description = parts[2];

                transactions.add(
                    new Transaction(category, amount, description)
                );
            }
        } catch (IOException e) {
            System.err.println(
                "Coach: Error loading history: " + e.getMessage()
            );
        }

        return transactions;
    }

    public void save(Transaction transaction) {
        try (
            FileWriter writer = new FileWriter(FILE_PATH, true);
            PrintWriter printer = new PrintWriter(writer)
        ) {
            printer.println(
                transaction.getCategory()
                    + ","
                    + transaction.getAmount()
                    + ","
                    + transaction.getDescription()
            );
        } catch (IOException e) {
            System.err.println(
                "Coach: Error saving data: " + e.getMessage()
            );
        }
    }
}