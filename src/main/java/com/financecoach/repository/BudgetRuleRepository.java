package com.financecoach.repository;

import com.financecoach.model.BudgetRule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class BudgetRuleRepository {
private final Path filePath;


public BudgetRuleRepository() {
    this(Path.of("src/main/resources/budget_rules.properties"));
}

public BudgetRuleRepository(Path filePath) {
    this.filePath = filePath;
}

public BudgetRule load() {
    if (!Files.exists(filePath)) {
        return BudgetRule.defaultRule();
    }

    Properties properties = new Properties();

    try (var reader = Files.newBufferedReader(filePath)) {
        properties.load(reader);

        double needs = Double.parseDouble(
            properties.getProperty("needs", "50")
        );
        double wants = Double.parseDouble(
            properties.getProperty("wants", "30")
        );
        double savings = Double.parseDouble(
            properties.getProperty("savings", "20")
        );

        return new BudgetRule(needs, wants, savings);

    } catch (IOException | IllegalArgumentException e) {
        System.err.println(
            "Coach: Could not load budget rules: " + e.getMessage()
        );
        return BudgetRule.defaultRule();
    }
}

public void save(BudgetRule rule) {
    Properties properties = new Properties();
    properties.setProperty("needs", Double.toString(rule.needs()));
    properties.setProperty("wants", Double.toString(rule.wants()));
    properties.setProperty("savings", Double.toString(rule.savings()));

    try {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (var writer = Files.newBufferedWriter(filePath)) {
            properties.store(writer, "Custom budget rules");
        }
    } catch (IOException e) {
        throw new IllegalStateException(
            "Could not save budget rules: " + e.getMessage(),
            e
        );
    }
}

public void reset() {
    save(BudgetRule.defaultRule());
}


}
