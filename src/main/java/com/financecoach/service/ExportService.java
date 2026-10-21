package com.financecoach.service;

import com.financecoach.model.MonthlyReport;
import com.financecoach.model.Transaction;
import com.financecoach.model.BudgetRule;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.List;

public class ExportService {

    private static final String EXPORT_PATH =
            "exports/monthly_summary.csv";
            public String renderMonthlyReport(MonthlyReport report) {
    return renderMonthlyReport(report, BudgetRule.defaultRule());
}
public Path exportMonthlyReport(MonthlyReport report) throws IOException {
    return exportMonthlyReport(report, BudgetRule.defaultRule());
}

public Path exportMonthlyReport(
        MonthlyReport report,
        BudgetRule budgetRule
) throws IOException {
    Path exportPath = Path.of(
        "exports",
        "monthly_report_" + report.month() + ".txt"
    );

    Files.createDirectories(exportPath.getParent());
    Files.writeString(
        exportPath,
        renderMonthlyReport(report, budgetRule)
    );

    return exportPath;
}

    public String renderMonthlyReport(
MonthlyReport report,
BudgetRule budgetRule
) {
StringBuilder text = new StringBuilder();


text.append("Monthly Financial Report - ")
    .append(report.month())
    .append(System.lineSeparator());

text.append("----------------------------------------")
    .append(System.lineSeparator());

appendAmount(text, "Total Income", report.income());
appendAmount(text, "Total Needs", report.needs());
appendAmount(text, "Total Wants", report.wants());
appendAmount(text, "Total Savings", report.savings());
appendAmount(text, "Total Debt", report.debt());
appendAmount(text, "Other", report.other());

text.append(System.lineSeparator())
    .append(budgetRule.equals(BudgetRule.defaultRule())
        ? "50/30/20 Breakdown"
        : "Custom Budget Breakdown")
    .append(System.lineSeparator());
appendPercentage(
    text,
    String.format(Locale.ROOT, "Needs (target %.0f%%)", budgetRule.needs()),
    report.needsPercent()
);

appendPercentage(
    text,
    String.format(Locale.ROOT, "Wants (target %.0f%%)", budgetRule.wants()),
    report.wantsPercent()
);

appendPercentage(
    text,
    String.format(
        Locale.ROOT,
        "Savings + Debt (target %.0f%%)",
        budgetRule.savings()
    ),
    report.savingsDebtPercent()
);

return text.toString();


}

    public void exportMonthlySummary(List<Transaction> transactions) {
    File directory = new File("exports");

    if (!directory.exists()) {
        directory.mkdirs();
    }

    try (PrintWriter writer = new PrintWriter(new FileWriter(EXPORT_PATH))) {
        writer.println("Date,Category,Description,Amount");

        for (Transaction transaction : transactions) {
           writer.printf(
                 Locale.ROOT,
                "%s,%s,%s,%s%n",
                transaction.getDate(),
                transaction.getCategory(),
                transaction.getDescription(),
                Double.toString(transaction.getAmount())
            );
        }

        System.out.println("Monthly summary exported to " + EXPORT_PATH);

    } catch (IOException e) {
        throw new IllegalStateException(
            "Could not export monthly summary: " + e.getMessage(), e
        );
    }
}
    private void appendAmount(StringBuilder text, String label, double amount) {
        text.append(String.format(Locale.ROOT, "%s: $%.2f%n", label, amount));
    }

    private void appendPercentage(StringBuilder text, String label, Double value) {
        text.append(label).append(": ");
        if (value == null) {
            text.append("N/A");
        } else {
            text.append(String.format(Locale.ROOT, "%.1f%%", value));
        }
        text.append(System.lineSeparator());
    }
}