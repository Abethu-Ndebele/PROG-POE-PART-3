/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.loginapplication;

/**
 *
 * @author Student
 */

import java.util.ArrayList;


import java.util.Scanner;

public class LoginApplication {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.println("======================================");
        System.out.println("        QuickChat System              ");
        System.out.println("======================================\n");
        
        LoginSystem auth = new LoginSystem("", "", "");
        String username, password, cellPhone, firstName, lastName;
        
        // ==================================
        // REGISTRATION
        // ==================================
        System.out.println("--- NEW USER REGISTRATION ---");
        
        do {
            System.out.print("Enter username: ");
            username = input.nextLine().trim();
            auth.userName = username;
            System.out.println(auth.getUserNameMessage());
        } while (!auth.checkUserName());
        System.out.println();
        
        do {
            System.out.print("Enter password: ");
            password = input.nextLine().trim();
            auth.password = password;
            System.out.println(auth.getPasswordMessage());
        } while (!auth.checkPasswordComplexity());
        System.out.println();
        
        do {
            System.out.print("Enter cell phone number (+27...): ");
            cellPhone = input.nextLine().trim();
            auth.cellPhoneNumber = cellPhone;
            System.out.println(auth.getCellMessage());
        } while (!auth.checkCellPhoneNumber());
        System.out.println();
        
        System.out.print("Enter first name: ");
        firstName = input.nextLine().trim();
        System.out.print("Enter last name: ");
        lastName = input.nextLine().trim();
        
        System.out.println("\n" + auth.registerUser(username, password, cellPhone, firstName, lastName));
        System.out.println();
        
        // ==================================
        // LOGIN
        // ==================================
        System.out.println("--- USER LOGIN ---");
        boolean loggedIn = false;
        
        while (!loggedIn) {
            String loginUsername;
            boolean usernameCorrect = false;
            while (!usernameCorrect) {
                System.out.print("Enter username: ");
                loginUsername = input.nextLine().trim();
                
                if (auth.checkUsernameMatch(loginUsername)) {
                    usernameCorrect = true;
                } else {
                    System.out.println("Username incorrect, please try again.\n");
                }
            }
            
            System.out.print("Enter password: ");
            String loginPassword = input.nextLine().trim();
            
            if (auth.checkPasswordMatch(loginPassword)) {
                loggedIn = true;
                System.out.println("\nWelcome " + firstName + " " + lastName + ", it is great to see you again.");
                System.out.println("Welcome to QuickChat.\n");
            } else {
                System.out.println("Password incorrect, please try again.\n");
            }
        }
        
        // ==================================
        // MAIN MENU
        // ==================================
        ArrayList<Message> allMessages = new ArrayList<>();
        int messageCounter = 0;
        
        System.out.print("How many messages do you wish to enter? ");
        int numToSend;
        try {
            numToSend = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number — defaulting to 5");
            numToSend = 5;
        }
        System.out.println();
        
        boolean running = true;
        while (running) {
            System.out.println("========== MAIN MENU ==========");
            System.out.println("Option 1) Send Messages");
            System.out.println("Option 2) Show recently sent messages");
            System.out.println("Option 3) Stored Messages Menu");
            System.out.println("Option 4) Quit");
            System.out.print("Select an option: ");
            
            String choice = input.nextLine().trim();
            System.out.println();
            
            switch (choice) {
                case "1":
                    if (messageCounter >= numToSend) {
                        System.out.println("You have entered all " + numToSend + " messages.\n");
                        break;
                    }
                    while (messageCounter < numToSend) {
                        Message msg = new Message();
                        messageCounter++;
                        System.out.println("--- Message " + messageCounter + " of " + numToSend + " ---");
                        
                        String recipient;
                        do {
                            System.out.print("Enter recipient cell number (+27...): ");
                            recipient = input.nextLine().trim();
                            System.out.println(msg.checkRecipientCell(recipient));
                        } while (!msg.isValidRecipientCell(recipient));
                        
                        String messageText;
                        do {
                            System.out.print("Enter your message (max 250 chars): ");
                            messageText = input.nextLine().trim();
                            System.out.println(msg.validateMessageLength(messageText));
                        } while (!msg.isMessageValidLength(messageText));
                        
                        String msgId = msg.generateMessageID();
                        String hash = msg.createMessageHash(msgId, messageCounter, messageText);
                        
                        System.out.println("\n--- Message Preview ---");
                        System.out.println(msg.printMessages(msgId, hash, recipient, messageText));
                        
                        System.out.println("\nChoose an option:");
                        System.out.println("1) Send Message");
                        System.out.println("0) Disregard Message");
                        System.out.println("2) Store Message to send later");
                        System.out.print("Your choice: ");
                        
                        int action;
                        try {
                            action = Integer.parseInt(input.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid choice — message disregarded.");
                            action = -1;
                        }
                        
                        String result = msg.sentMessage(action);
                        System.out.println("\n" + result + "\n");
                        
                        msg.setMessageID(msgId);
                        msg.setRecipientCell(recipient);
                        msg.setMessageText(messageText);
                        msg.setMessageHash(hash);
                        
                        allMessages.add(msg);
                        MessageStorageManager.routeMessage(msg);
                        
                        if (messageCounter >= numToSend) {
                            System.out.println("All " + numToSend + " messages processed!\n");
                            break;
                        }
                    }
                    break;
                    
                case "2":
                    System.out.println("--- Recently Sent Messages ---");
                    if (MessageStorageManager.sentCount == 0) {
                    System.out.println("No sent messages yet.\n");

                    } else {
                        // ✅ NEW — Loop only up to sentCount
                    for (int i = 0; i < MessageStorageManager.sentCount; i++) {
                    Message m = MessageStorageManager.sentMessages[i];
                    System.out.println("[" + m.getMessageID() + "] " + m.getMessageText());
                    }

                        System.out.println();
                    }
                    break;
                    
                case "3":
                    MessageStorageManager.showStoredMessagesMenu(input);
                    break;
                    
                case "4":
                    System.out.println("Total messages entered: " + allMessages.size());
                    System.out.println("Goodbye!");
                    running = false;
                    input.close();
                    return;
                    
                default:
                    System.out.println("Invalid option — please try again.\n");
            }
        }
    }
}

