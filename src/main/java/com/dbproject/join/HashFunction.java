package com.dbproject.join;

public class HashFunction {
    private final int numPartitions;
    public HashFunction(int numPartitions) {
        this.numPartitions = numPartitions;
    }
    // partitioning hash 
    public int hash1(int key) {
        return Math.floorMod(key, numPartitions);
    }
    
}
