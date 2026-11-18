/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginapplication;

/**
 *
 * @author Student
 */


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Scanner;



public class MessageStorageManager {
    
    // ===== PARALLEL ARRAYS — Rubric Requirement =====
    public static final int MAX_MESSAGES = 100;
    public static Message[] sentMessages = new Message[MAX_MESSAGES];
    public static Message[] disregardedMessages = new Message[MAX_MESSAGES];
    public static Message[] storedMessages = new Message[MAX_MESSAGES];
    public static String[] messageHashes = new String[MAX_MESSAGES];
    
    // Counters to track actual number of items
    public static int sentCount = 0;
    public static int disregardedCount = 0;
    public static int storedCount = 0;

    // ==================================
    // Route message to correct array
    // ==================================
    public static void routeMessage(Message msg) {
        if (msg == null || msg.getStatus() == null) return;
        
        switch (msg.getStatus()) {
            case "Sent":
                sentMessages[sentCount] = msg;
                sentCount++;
                break;
            case "Disregarded":
                disregardedMessages[disregardedCount] = msg;
                disregardedCount++;
                break;
            case "Stored":
                storedMessages[storedCount] = msg;
                messageHashes[storedCount] = msg.getMessageHash();
                storedCount++;
                break;
        }
    }

    // ==================================
    // PART 3 — Requirement 1
    // Display sender and recipient of ALL stored messages
    // ==================================
    public static void displayAllRecipients() {
        System.out.println("\n===== ALL STORED MESSAGES =====");
        if (storedCount == 0) {
            System.out.println("No stored messages found.");
            return;
        }
        System.out.printf("%-15s | %-20s%n", "Message ID", "Recipient");
        System.out.println("----------------------------------------");
        for (int i = 0; i < storedCount; i++) {
            Message m = storedMessages[i];
            System.out.printf("%-15s | %-20s%n", 
                safeStr(m.getMessageID()), 
                safeStr(m.getRecipientCell()));
        }
    }

    // ==================================
    // PART 3 — Requirement 2
    // Display the LONGEST stored message
    // ==================================
    public static void displayLongestMessage() {
        System.out.println("\n===== LONGEST STORED MESSAGE =====");
        if (storedCount == 0) {
            System.out.println("No stored messages found.");
            return;
        }
        Message longest = storedMessages[0];
        for (int i = 1; i < storedCount; i++) {
            Message m = storedMessages[i];
            if (m.getMessageText() != null 
                && longest.getMessageText() != null
                && m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }
        System.out.println("Message ID: " + safeStr(longest.getMessageID()));
        System.out.println("Recipient: " + safeStr(longest.getRecipientCell()));
        System.out.println("Message: \"" + safeStr(longest.getMessageText()) + "\"");
        System.out.println("Length: " + longest.getMessageText().length() + " characters");
    }

    // ==================================
    // PART 3 — Requirement 3
    // Search by Message ID
    // ==================================
    public static void searchByMessageId(String searchId) {
        System.out.println("\n===== SEARCH RESULTS =====");
        boolean found = false;
        for (int i = 0; i < storedCount; i++) {
            Message m = storedMessages[i];
            if (m.getMessageID() != null 
                && m.getMessageID().equalsIgnoreCase(searchId)) {
                System.out.println("Recipient: " + safeStr(m.getRecipientCell()));
                System.out.println("Message: \"" + safeStr(m.getMessageText()) + "\"");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("No message found with ID: " + searchId);
        }
    }

    // ==================================
    // PART 3 — Requirement 4
    // Search by Recipient Number
    // ==================================
    public static void searchByRecipient(String recipientNumber) {
        System.out.println("\n===== MESSAGES TO: " + recipientNumber + " =====");
        boolean found = false;
        for (int i = 0; i < storedCount; i++) {
            Message m = storedMessages[i];
            if (m.getRecipientCell() != null 
                && m.getRecipientCell().equals(recipientNumber)) {
                System.out.println("\"" + safeStr(m.getMessageText()) + "\"");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No messages found for this recipient.");
        }
    }

    // ==================================
    // PART 3 — Requirement 5
    // Delete by Message Hash
    // ==================================
    public static void deleteByHash(String hashToDelete) {
        int index = -1;
        for (int i = 0; i < storedCount; i++) {
            if (hashToDelete.equals(messageHashes[i])) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("No message found with that hash.");
            return;
        }
        
        // Shift remaining elements down
        for (int i = index; i < storedCount - 1; i++) {
            storedMessages[i] = storedMessages[i + 1];
            messageHashes[i] = messageHashes[i + 1];
        }
        
        // Clear the last slot and decrement count
        storedMessages[storedCount - 1] = null;
        messageHashes[storedCount - 1] = null;
        storedCount--;
        
        System.out.println("Message successfully deleted.");
    }

    // ==================================
    // PART 3 — Requirement 6
    // Full Report
    // ==================================
    public static void displayFullReport() {
        System.out.println("\n\n==================================================");
        System.out.println("           FULL MESSAGE REPORT                     ");
        System.out.println("==================================================");

        System.out.println("\n--- SENT MESSAGES ---");
        if (sentCount == 0) {
            System.out.println("No sent messages.");
        } else {
            for (int i = 0; i < sentCount; i++) {
                Message m = sentMessages[i];
                System.out.println("[" + safeStr(m.getMessageID()) + "] To: " + 
                    safeStr(m.getRecipientCell()) + " — \"" + 
                    safeStr(m.getMessageText()) + "\"");
            }
        }

        System.out.println("\n--- DISREGARDED MESSAGES ---");
        if (disregardedCount == 0) {
            System.out.println("No disregarded messages.");
        } else {
            for (int i = 0; i < disregardedCount; i++) {
                Message m = disregardedMessages[i];
                System.out.println("[" + safeStr(m.getMessageID()) + "] \"" + 
                    safeStr(m.getMessageText()) + "\"");
            }
        }

        System.out.println("\n--- STORED MESSAGES ---");
        if (storedCount == 0) {
            System.out.println("No stored messages.");
        } else {
            System.out.printf("%-14s | %-16s | %-10s | %s%n", 
                "Message ID", "Recipient", "Hash", "Message");
            System.out.println("-----------------------------------------------------------");
            for (int i = 0; i < storedCount; i++) {
                Message m = storedMessages[i];
                System.out.printf("%-14s | %-16s | %-10s | %s%n",
                    safeStr(m.getMessageID()),
                    safeStr(m.getRecipientCell()),
                    safeStr(m.getMessageHash()),
                    safeStr(m.getMessageText()));
            }
        }
        System.out.println("==================================================\n");
    }

    // ==================================
    // PART 3 — Requirement 7
    // Read JSON file into arrays
    // Gson library source: https://github.com/google/gson
    // ==================================
    public static void readMessagesFromJson(String filePath) {
        System.out.println("\n===== READ MESSAGES FROM JSON =====");
        try (java.io.Reader reader = java.nio.file.Files.newBufferedReader(java.nio.file.Paths.get(filePath))) {
            
            java.lang.reflect.Type messageListType = new TypeToken<ArrayList<Message>>(){}.getType();
            ArrayList<Message> loadedList = new Gson().fromJson(reader, messageListType);
            
            if (loadedList == null || loadedList.isEmpty()) {
                System.out.println("No messages found in JSON file.");
                return;
            }
            
            int loadedCount = 0;
            for (Message m : loadedList) {
                if (storedCount < MAX_MESSAGES) {
                    storedMessages[storedCount] = m;
                    messageHashes[storedCount] = m.getMessageHash();
                    storedCount++;
                    loadedCount++;
                }
            }
            System.out.println("Successfully loaded " + loadedCount + " messages from JSON file.");
            
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Error: File not found — " + filePath);
        } catch (Exception e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }

    // ==================================
    // Stored Messages Sub-Menu
    // ==================================
    public static void showStoredMessagesMenu(Scanner input) {
        while (true) {
            System.out.println("\n========== STORED MESSAGES MENU ==========");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message by Message ID");
            System.out.println("4) Search messages by recipient number");
            System.out.println("5) Delete a message using Message Hash");
            System.out.println("6) Display full report");
            System.out.println("7) Load messages from JSON file");
            System.out.println("0) Return to Main Menu");
            System.out.print("Select an option: ");

            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    displayAllRecipients();
                    break;
                case "2":
                    displayLongestMessage();
                    break;
                case "3":
                    System.out.print("Enter Message ID to search: ");
                    String idSearch = input.nextLine().trim();
                    searchByMessageId(idSearch);
                    break;
                case "4":
                    System.out.print("Enter recipient number to search: ");
                    String recipSearch = input.nextLine().trim();
                    searchByRecipient(recipSearch);
                    break;
                case "5":
                    System.out.print("Enter Message Hash to delete: ");
                    String hashDelete = input.nextLine().trim();
                    deleteByHash(hashDelete);
                    break;
                case "6":
                    displayFullReport();
                    break;
                case "7":
                    System.out.print("Enter JSON file path (e.g., messages.json): ");
                    String path = input.nextLine().trim();
                    readMessagesFromJson(path);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option — try again.\n");
            }
        }
    }

    // Helper method — prevents null errors
    private static String safeStr(String s) {
        return s == null ? "" : s;
    }
}
