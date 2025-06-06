/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.pogi.cashregistermacs;

/**
 *
 * @author huawei
 */

import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.lang.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class CASHREGISTERMACS {

    static Scanner CYC = new Scanner(System.in);
    static String currentUser = "";

    static int[] menuNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    static String[] menuProducts = {"Chicken Biryani", "Beef Biryani", "Chicken Shawarma", "Beef Shawarma", "Khuboz Bread", "Toum (Garlic Sauce)", "Extra Biryani Rice", "Masala (Indian Spice Sauce)", "Salsa"};
    static double[] menuPrices = {170.00, 200.00, 120.00, 140.00, 35.00, 10.00, 80.00, 10.00, 10.00};

    static ArrayList<String> users = new ArrayList<>();
    static ArrayList<String> passwords = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Welcome to Arbab Biryani ===");
        System.out.println("Please sign up first before ordering.");

        signup();

        System.out.println("\nNow, please log in to Arbab Biryani.");

        login();

        System.out.println("\nLogin successful! Proceeding for you to order your food...");

        boolean anotherTransaction = true;

        while (anotherTransaction) {
            ArrayList<String> cartItems = new ArrayList<>();
            ArrayList<Double> cartItemPrice = new ArrayList<>();
            ArrayList<Double> cartTotalPrice = new ArrayList<>();
            ArrayList<Integer> cartQuantities = new ArrayList<>();

            while (true) {
                displayMenu();
                System.out.println("\n1. Order  2. View Cart  3. Remove Item  4. Checkout  5. Exit");
                System.out.print("Choose an option: ");
                int choice = CYC.nextInt();
                CYC.nextLine();

                switch (choice) {
                    case 1:
                        addItemToCart(cartItems, cartItemPrice, cartTotalPrice, cartQuantities);
                        break;
                    case 2:
                        viewCart(cartItems, cartItemPrice, cartTotalPrice, cartQuantities);
                        break;
                    case 3:
                        removeItemFromCart(cartItems, cartItemPrice, cartTotalPrice, cartQuantities);
                        break;
                    case 4:
                        if (cartItems.isEmpty()) {
                            System.out.println("Your Cart is empty. Add items before checkout.");
                        } else {
                            double total = checkout(cartItems, cartTotalPrice);
                            double[] paymentInfo = processPayment(total);
                            double payment = paymentInfo[0];
                            double change = paymentInfo[1];
                            logTransaction(currentUser, cartItems, cartQuantities, cartItemPrice, total, payment, change);
                        }
                        break;
                    case 5:
                        System.out.println("Thank you for visiting Arbab Biryani!");
                        anotherTransaction = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }

                if (choice == 4 || choice == 5) {
                    break;
                }
            }

            if (anotherTransaction) {
                System.out.print("Would you like to perform another transaction or order? (yes/no): ");
                String response = CYC.next().toLowerCase();
                anotherTransaction = response.equals("yes");
            }
        }

        System.out.println("Thank you for dining at Arbab Biryani!! Please Come Again!!");
        CYC.close();
    }

    public static void signup() {
        String emailRegex = "[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        String usernameRegex = "^[a-zA-Z0-9]{5,15}$";
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$";

        while (true) {
            System.out.print("Enter your email: ");
            String email = CYC.nextLine();
            System.out.print("Create a username (5-15 alphanumeric characters): ");
            String username = CYC.nextLine();
            System.out.print("Create a password (8-20 characters, 1 uppercase letter, 1 number): ");
            String password = CYC.nextLine();

            if (!Pattern.matches(emailRegex, email)) {
                System.out.println("Invalid email format. Please enter a valid email.");
                continue;
            }
            if (!Pattern.matches(usernameRegex, username)) {
                System.out.println("Invalid username. Please follow the format.");
                continue;
            }
            if (!Pattern.matches(passwordRegex, password)) {
                System.out.println("Invalid password. Please follow the format.");
                continue;
            }

            users.add(username);
            passwords.add(password);
            System.out.println("Signup successful!");
            break;
        }
    }

    public static void login() {
        while (true) {
            System.out.print("Enter your username: ");
            String loginUsername = CYC.nextLine();
            System.out.print("Enter your password: ");
            String loginPassword = CYC.nextLine();

            boolean found = false;

            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).equals(loginUsername) && passwords.get(i).equals(loginPassword)) {
                    currentUser = loginUsername;
                    found = true;
                    break;
                }
            }

            if (found) {
                break;
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        }
    }

    public static void displayMenu() {
        System.out.println("\n<_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_->");
        System.out.println("Welcome to ARBAB BIRYANI!! ");
        System.out.println("<_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_->");
        System.out.println("\nARBAB BIRYANI MENU:");

        for (int i = 0; i < menuProducts.length; i++) {
            System.out.println(menuNumbers[i] + ". " + menuProducts[i] + " - PHP " + menuPrices[i]);
        }
    }

    public static void addItemToCart(ArrayList<String> cartItems, ArrayList<Double> cartItemPrice, ArrayList<Double> cartTotalPrice, ArrayList<Integer> cartQuantities) {
        System.out.print("Enter menu number: ");
        int menuChoice = CYC.nextInt();
        CYC.nextLine();

        if (menuChoice >= 1 && menuChoice <= menuProducts.length) {
            String itemName = menuProducts[menuChoice - 1];
            double itemPrice = menuPrices[menuChoice - 1];
            System.out.print("Enter No. of Items: ");
            int quantity = CYC.nextInt();
            CYC.nextLine();

            cartItems.add(itemName);
            cartItemPrice.add(itemPrice);
            cartQuantities.add(quantity);
            cartTotalPrice.add(itemPrice * quantity);

            System.out.println(itemName + " added to cart!");
        } else {
            System.out.println("Invalid item choice.");
        }
    }

    public static void viewCart(ArrayList<String> cartItems, ArrayList<Double> cartItemPrice, ArrayList<Double> cartTotalPrice, ArrayList<Integer> cartQuantities) {
        System.out.println("\nCurrent Cart:");

        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        for (int i = 0; i < cartItems.size(); i++) {
            System.out.println(cartItems.get(i) + " | No. of Items: " + cartQuantities.get(i) + " | Price: PHP " + cartItemPrice.get(i) + " | Total: PHP " + cartTotalPrice.get(i));
        }
    }

    public static void removeItemFromCart(ArrayList<String> cartItems, ArrayList<Double> cartItemPrice, ArrayList<Double> cartTotalPrice, ArrayList<Integer> cartQuantities) {
        System.out.println("\nCurrent Cart:");
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        for (int i = 0; i < cartItems.size(); i++) {
            System.out.println((i + 1) + ". " + cartItems.get(i) + " | No. of Items: " + cartQuantities.get(i) + " | Price: PHP " + cartItemPrice.get(i) + " | Total: PHP " + cartTotalPrice.get(i));
        }

        System.out.print("\nEnter the number of the item to remove: ");
        int removeIndex = CYC.nextInt();
        CYC.nextLine();

        if (removeIndex >= 1 && removeIndex <= cartItems.size()) {
            System.out.println(cartItems.get(removeIndex - 1) + " removed from cart.");
            cartItems.remove(removeIndex - 1);
            cartItemPrice.remove(removeIndex - 1);
            cartTotalPrice.remove(removeIndex - 1);
            cartQuantities.remove(removeIndex - 1);
        } else {
            System.out.println("Invalid selection. No item removed.");
        }
    }

    public static double checkout(ArrayList<String> cartItems, ArrayList<Double> cartTotalPrice) {
        double total = 0;
        System.out.println("\nReceipt:");

        for (int i = 0; i < cartItems.size(); i++) {
            System.out.println(cartItems.get(i) + " | Total: PHP " + cartTotalPrice.get(i));
            total += cartTotalPrice.get(i);
        }

        System.out.print("Are you a PWD or Senior Citizen (yes/no)?: ");
        String discountApply = CYC.next().toLowerCase();
        CYC.nextLine();

        if (discountApply.equals("yes")) {
            double discountAmount = total * 0.15;
            total -= discountAmount;
            System.out.println("Discount has been applied. (PHP " + discountAmount + ") ");
        }

        System.out.println("Total Amount: PHP " + total);
        return total;
    }

    public static double[] processPayment(double total) {
        double payment;
        do {
            System.out.print("Enter payment amount: PHP ");
            payment = CYC.nextDouble();
            CYC.nextLine();

            if (payment < total) {
                System.out.println("Insufficient amount. Please enter at least PHP " + total);
            }
        } while (payment < total);

        double change = payment - total;
        System.out.println("Your change: PHP " + change);
        System.out.println("Enjoy your meal!");
        return new double[]{payment, change};
    }

    public static void logTransaction(String username, ArrayList<String> cartItems, ArrayList<Integer> cartQuantities,
                                      ArrayList<Double> cartItemPrice, double totalAmount, double payment, double change) {
        try (FileWriter sulat = new FileWriter("transactions.txt", true)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            sulat.write("=== Transaction Record ===\n");
            sulat.write("Date & Time: " + dtf.format(now) + "\n");
            sulat.write("Username: " + username + "\n");
            sulat.write("Items Purchased:\n");

            for (int i = 0; i < cartItems.size(); i++) {
                sulat.write(" - " + cartItems.get(i) + " | Quantity: " + cartQuantities.get(i)
                        + " | Price: PHP " + cartItemPrice.get(i) + "\n");
            }

            sulat.write("Total Amount: PHP " + totalAmount + "\n");
            sulat.write("Payment Amount: PHP " + payment + "\n");
            sulat.write("Change: PHP " + change + "\n");
            sulat.write("===========================\n\n");
        } catch (IOException e) {
            System.out.println("Error writing transaction: " + e.getMessage());
        }
    }
}
