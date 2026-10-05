package com.financecoach.service;

import com.financecoach.model.Transaction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ExportService {

    private static final String EXPORT_PATH =
            "exports/monthly_summary.csv";

    public void exportMonthlySummary(List<Transaction> transactions) {

        File exportFile = new File(EXPORT_PATH);

        File parentFolder = exportFile.getParentFile();

        if (parentFolder != null) {
            parentFolder.mkdirs();
        }

        try (
            FileWriter writer = new FileWriter(exportFile);
            PrintWriter printer = new PrintWriter(writer)
        ) {

            printer.println("Date,Category,Description,Amount");

            for (Transaction transaction : transactions) {

                printer.println(
                    transaction.getDate()
                        + ","
                        + transaction.getCategory()
                        + ","
                        + transaction.getDescription()
                        + ","
                        + transaction.getAmount()
                );
            }

            System.out.println(
                "Monthly summary exported successfully to "
                    + EXPORT_PATH
            );

        } catch (IOException e) {

            System.err.println(
                "Coach: Error exporting summary: "
                    + e.getMessage()
            );
        }
    }
}