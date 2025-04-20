package com.dbproject;

import com.dbproject.btree.BTreeTester;
import com.dbproject.join.JoinTester;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        System.out.println("Database Algo Project\n");
        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Run B+ Tree implementation tests");
            System.out.println("2. Run Hash Join implementation tests");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    System.out.println("\nRunning B+ Tree implementation tests...\n");
                    runBTreeTests();
                    break;
                case "2":
                    System.out.println("\nRunning Hash Join implementation tests...\n");
                    runJoinTests();
                    break;
                case "3":
                    running = false;
                    System.out.println("\nExited.");
                    break;
                default:
                    System.out.println("\nInvalid choice. Choose correct option.");
                    break;
            }
        }
        
        scanner.close();
    }
    
    private static void runBTreeTests() {
        BTreeTester tester = new BTreeTester();
        tester.runTests();
    }
    

    private static void runJoinTests() {
        JoinTester tester = new JoinTester();
        tester.runTests();
    }
}