package com.financecoach.utils;

import java.util.Scanner;

public class InputUtils implements AutoCloseable {
    private static final String DEFAULT_INT_ERROR =
        "Please enter a valid integer.";

    private static final String DEFAULT_DOUBLE_ERROR =
        "Please enter a valid number.";

    private final Scanner scanner;

    public InputUtils(Scanner scanner) {
        this.scanner = scanner;
    }

    // * [+] Read Methods

    /**
     * Prints the prompt and returns user input as a text string.
     *
     * @param prompt The {@code String} to print to the console.
     * @return A {@code String} entered by the user.
     */
    public String askString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Prints the prompt and returns user input as a whole number.
     * Uses a default error message if invalid input is entered.
     *
     * @param prompt The {@code String} to print to the console.
     * @return An {@code int} entered by the user.
     */
    public int askInt(String prompt) {
        return askInt(prompt, DEFAULT_INT_ERROR);
    }

    /**
     * Prints the prompt and returns user input as a whole number.
     *
     * @param prompt The {@code String} to print to the console.
     * @param errMsg The error message to print if the input is not a valid integer.
     * @return An {@code int} entered by the user.
     */
    public int askInt(String prompt, String errMsg) {
        while (true) {
            try {
                return Integer.parseInt(askString(prompt));
            } catch (NumberFormatException e) {
                System.out.println(errMsg);
            }
        }
    }

    /**
     * Prints the prompt and returns user input as a decimal number.
     * Uses a default error message if invalid input is entered.
     *
     * @param prompt The {@code String} to print to the console.
     * @return A {@code double} entered by the user.
     */
    public double askDouble(String prompt) {
        return askDouble(prompt, DEFAULT_DOUBLE_ERROR);
    }

    /**
     * Prints the prompt and returns user input as a decimal number.
     *
     * @param prompt The {@code String} to print to the console.
     * @param errMsg The error message to print if the input is not a valid double.
     * @return A {@code double} entered by the user.
     */
    public double askDouble(String prompt, String errMsg) {
        while (true) {
            try {
                return Double.parseDouble(askString(prompt));
            } catch (NumberFormatException e) {
                System.out.println(errMsg);
            }
        }
    }

    // * [-] Read Methods

    @Override
    public void close() {
        scanner.close();
    }
}