package com.dbproject.btree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BTreeTester {
    private final TreeBuilder treeBuilder;
    private final Random random;
    

    public BTreeTester() {
        this.treeBuilder = new TreeBuilder();
        this.random = new Random();
    }

    public List<Integer> generateUniqueKeys(int count, int min, int max) {

        Set<Integer> s = new HashSet<>();  // to eliminate duplicates
        while (s.size() < count) {
            s.add(random.nextInt(max - min + 1) + min);
        }
        return new ArrayList<>(s);
    }
    
    public void runTests() {
        System.out.println("Part 1: Generating 10,000 Unique records.");
        List<Integer> records = generateUniqueKeys(10000, 100000, 200000);
        System.out.println("Records generated successfully.\n");
        
        System.out.println("Part 2: Building B+ trees.");
        System.out.println("Building dense B+ tree of order 13.");
        BPlusTree denseTree13 = treeBuilder.buildDenseTree(records, 13);
        
        System.out.println("Building sparse B+ tree of order 13.");
        BPlusTree sparseTree13 = treeBuilder.buildSparseTree(records, 13);
        
        System.out.println("Building dense B+ tree of order 24.");
        BPlusTree denseTree24 = treeBuilder.buildDenseTree(records, 24);
        
        System.out.println("Building sparse B+ tree of order 24.");
        BPlusTree sparseTree24 = treeBuilder.buildSparseTree(records, 24);
        System.out.println("All trees built successfully.\n");
        
        System.out.println("Part 3 & 4: Testing B+ tree operations.");
        
        // Test insertion on dense trees
        System.out.println("\nTesting 2 insertions on dense tree of order 13:");
        testInsertions(denseTree13, 2);
        
        System.out.println("\nTesting 2 insertions on dense tree of order 24:");
        testInsertions(denseTree24, 2);
        
        // Test deletion on sparse trees
        System.out.println("\nTesting 2 deletions on sparse tree of order 13:");
        testDeletions(sparseTree13, 2);
        
        System.out.println("\nTesting 2 deletions on sparse tree of order 24:");
        testDeletions(sparseTree24, 2);
        
        // Test additional operations
        System.out.println("\nTesting 5 additional operations on dense tree of order 13:");
        testAdditionalOperations(denseTree13, 5);
        
        System.out.println("\nTesting 5 additional operations on sparse tree of order 13:");
        testAdditionalOperations(sparseTree13, 5);
        
        System.out.println("\nTesting 5 additional operations on dense tree of order 24:");
        testAdditionalOperations(denseTree24, 5);
        
        System.out.println("\nTesting 5 additional operations on sparse tree of order 24:");
        testAdditionalOperations(sparseTree24, 5);
        
        // Test search operations
        System.out.println("\nTesting 5 search operations on dense tree of order 13:");
        testSearches(denseTree13, 5);
        
        System.out.println("\nTesting 5 search operations on sparse tree of order 13:");
        testSearches(sparseTree13, 5);
        
        System.out.println("\nTesting 5 search operations on dense tree of order 24:");
        testSearches(denseTree24, 5);
        
        System.out.println("\nTesting 5 search operations on sparse tree of order 24:");
        testSearches(sparseTree24, 5);
    }
    
    public int generateRandomKey(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    // c1
    private void testInsertions(BPlusTree tree, int count) {
        for (int i = 0; i < count; i++) {
            int key = generateRandomKey(100000, 200000); // generates random key
            
            System.out.println("\nBefore insertion of key " + key + ":");
            tree.print();
            
            tree.insert(key);
            
            System.out.println("\nAfter insertion of key " + key + ":");
            tree.print();
        }
    }
    // c2
    private void testDeletions(BPlusTree tree, int count) {
        for (int i = 0; i < count; i++) {
            // Find a key in the tree to delete
            int key = findExistingKey(tree);
            
            System.out.println("\nBefore deletion of key " + key + ":");
            tree.print();
            
            tree.delete(key);
            
            System.out.println("\nAfter deletion of key " + key + ":");
            tree.print();
        }
    }
    //c3
    private void testAdditionalOperations(BPlusTree tree, int count) {
        for (int i = 0; i < count; i++) {
            boolean isInsertion = random.nextBoolean();
            
            if (isInsertion) {
                int key = generateRandomKey(100000, 200000);
                
                System.out.println("\nBefore insertion of key " + key + ":");
                tree.print();
                
                tree.insert(key);
                
                System.out.println("\nAfter insertion of key " + key + ":");
                tree.print();
            } else {
                // Find a key in the tree to delete
                int key = findExistingKey(tree);
                
                System.out.println("\nBefore deletion of key " + key + ":");
                tree.print();
                
                tree.delete(key);
                
                System.out.println("\nAfter deletion of key " + key + ":");
                tree.print();
            }
        }
    }
    
    // c4
    private void testSearches(BPlusTree tree, int count) {
        for (int i = 0; i < count; i++) {
            int key = generateRandomKey(100000, 200000);
            
            System.out.println("\nSearching for key " + key + ":");
            boolean found = tree.search(key);
            
            System.out.println("Key " + key + " found: " + found);
            
            // Also testing a range search
            int lowerBound = key - 1000;
            int upperBound = key + 1000;
            
            System.out.println("\nPerforming range search [" + lowerBound + ", " + upperBound + "]:");
            List<Integer> rangeResult = tree.rangeSearch(lowerBound, upperBound);
            
            System.out.println("Found " + rangeResult.size() + " keys in the range");
            if (!rangeResult.isEmpty()) {
                System.out.print("Sample keys: ");
                int size = Math.min(5, rangeResult.size());
                for (int j = 0; j < size; j++) {
                    System.out.print(rangeResult.get(j));
                    if (j < size - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        }
    }
    
    private int findExistingKey(BPlusTree tree) {
        int key;
        do {
            key = generateRandomKey(100000, 200000);
        } while (!tree.search(key));
        
        return key;
    }
}