package com.financecoach.model;

public record BudgetRule(
double needs,
double wants,
double savings
) {
public BudgetRule {
if (!Double.isFinite(needs)
|| !Double.isFinite(wants)
|| !Double.isFinite(savings)) {
throw new IllegalArgumentException(
"Budget percentages must be finite numbers."
);
}


    if (needs < 0 || wants < 0 || savings < 0) {
        throw new IllegalArgumentException(
            "Budget percentages cannot be negative."
        );
    }

    if (Math.abs(needs + wants + savings - 100.0) > 0.000001) {
        throw new IllegalArgumentException(
            "Budget percentages must add up to 100."
        );
    }
}

public static BudgetRule defaultRule() {
    return new BudgetRule(50, 30, 20);
}


}
