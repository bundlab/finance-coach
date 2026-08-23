package com.financecoach;
import com.financecoach.engine.FinanceEngine;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FinanceEngine coach = new FinanceEngine();
        
        System.out.println("Welcome to your AI Personal Finance Coach!");

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense (Needs, Wants, Debt, Savings)");
            System.out.println("3. Get AI Coaching Report");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            
            // Check if the input is actually a number to prevent crashes
            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a number (1-4).");
                scanner.next(); // clear invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // IMPORTANT: This "flushes" the Enter key from the buffer

            if (choice == 4) break;

            switch (choice) {
                case 1:
                    System.out.print("Enter Income Amount: ");
                    double inc = scanner.nextDouble();
                    scanner.nextLine(); // Flush buffer
                    coach.addTransaction("Income", inc, "Monthly Salary");
                    System.out.println("Income added successfully!");
                    break;
                case 2:
                    System.out.print("Category (Needs/Wants/Debt/Savings): ");
                    String cat = scanner.nextLine(); // Use nextLine to be safe
                    System.out.print("Amount: ");
                    double amt = scanner.nextDouble();

                    if(amt<0)
                    {
                        System.out.println("Enter the valid expense amount! ");
                        scanner.nextLine();
                        break;
                    }

                    scanner.nextLine(); // Flush buffer
                    coach.addTransaction(cat, amt, "User Expense");
                    System.out.println("Expense logged.");
                    break;
                case 3:
                    coach.generateCoachingReport();
                    break;
                default:
                    System.out.println("Invalid choice. Try 1-4.");
            }
        }
        scanner.close();
        System.out.println("Stay financially healthy! Goodbye.");
    }
}