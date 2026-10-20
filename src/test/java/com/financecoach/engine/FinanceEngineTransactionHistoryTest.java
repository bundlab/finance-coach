
package com.financecoach.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.financecoach.model.Transaction;
import com.financecoach.repository.TransactionRepository;

class FinanceEngineTransactionHistoryTest {

    @TempDir
    Path tempDir;

    private FinanceEngine createEngine(String csv) throws Exception {
        Path dataFile = tempDir.resolve("transactions.csv");
        Files.writeString(dataFile, csv);
        return new FinanceEngine(new TransactionRepository(dataFile));
    }

    @Test
    void returnsTransactionsNewestFirstAndUndatedLast() throws Exception {
        FinanceEngine engine = createEngine(
                "Needs,100,Rent,2026-10-01\n"
                        + "Wants,50,Leisure,2026-10-15\n"
                        + "Income,1000,Salary,2026-10-10\n"
                        + "Savings,20,Legacy transaction\n"
        );

        List<Transaction> history =
                engine.getTransactionHistory(null, null, null);

        assertEquals(4, history.size());
        assertEquals(
                LocalDate.of(2026, 10, 15),
                history.get(0).getDate()
        );
        assertEquals(
                LocalDate.of(2026, 10, 10),
                history.get(1).getDate()
        );
        assertEquals(
                LocalDate.of(2026, 10, 1),
                history.get(2).getDate()
        );
        assertNull(history.get(3).getDate());
    }

    @Test
    void filtersCategoryWithoutCaseSensitivity() throws Exception {
        FinanceEngine engine = createEngine(
                "Needs,100,Rent,2026-10-01\n"
                        + "Wants,50,Leisure,2026-10-15\n"
                        + "Needs,200,Groceries,2026-10-20\n"
        );

        List<Transaction> history =
                engine.getTransactionHistory("nEeDs", null, null);

        assertEquals(2, history.size());
        assertEquals("Groceries", history.get(0).getDescription());
        assertEquals("Rent", history.get(1).getDescription());
    }

    @Test
    void filtersInclusiveDateRange() throws Exception {
        FinanceEngine engine = createEngine(
                "Needs,100,Before,2026-09-30\n"
                        + "Needs,200,Start,2026-10-01\n"
                        + "Wants,50,Middle,2026-10-15\n"
                        + "Savings,20,End,2026-10-31\n"
                        + "Income,1000,After,2026-11-01\n"
                        + "Needs,25,Undated\n"
        );

        List<Transaction> history = engine.getTransactionHistory(
                null,
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 31)
        );

        assertEquals(3, history.size());
        assertEquals("End", history.get(0).getDescription());
        assertEquals("Middle", history.get(1).getDescription());
        assertEquals("Start", history.get(2).getDescription());
    }

    @Test
    void combinesCategoryAndDateFilters() throws Exception {
        FinanceEngine engine = createEngine(
                "Needs,100,Rent,2026-10-01\n"
                        + "Needs,200,Groceries,2026-10-20\n"
                        + "Wants,50,Leisure,2026-10-15\n"
        );

        List<Transaction> history = engine.getTransactionHistory(
                "Needs",
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 31)
        );

        assertEquals(1, history.size());
        assertEquals("Groceries", history.get(0).getDescription());
    }

    @Test
    void returnsEmptyListWhenNoTransactionsMatch() throws Exception {
        FinanceEngine engine = createEngine(
                "Needs,100,Rent,2026-10-01\n"
        );

        List<Transaction> history =
                engine.getTransactionHistory("Wants", null, null);

        assertEquals(0, history.size());
    }
}
