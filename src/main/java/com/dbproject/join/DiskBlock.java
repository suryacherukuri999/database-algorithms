package com.dbproject.join;

import java.util.ArrayList;
import java.util.List;

public class DiskBlock {
    private static final int MAX_TUPLES = 8;
    private final List<Tuple> tuples;

    public DiskBlock() {
        this.tuples = new ArrayList<>();
    }

    public DiskBlock(List<Tuple> tuples) {
        if (tuples.size() > MAX_TUPLES) {
            throw new IllegalArgumentException("Block can hold at most " + MAX_TUPLES + " tuples");
        }
        this.tuples = new ArrayList<>(tuples);
    }

    public static int getMaxTuples() {
        return MAX_TUPLES;
    }

    public List<Tuple> getTuples() {
        return tuples;
    }

    public void addTuple(Tuple t) {
        if (tuples.size() >= MAX_TUPLES) {
            throw new IllegalStateException("Block is full");
        }
        tuples.add(t);
    }

    public boolean isFull() {
        return tuples.size() >= MAX_TUPLES;
    }

    public boolean isEmpty() {
        return tuples.isEmpty();
    }

    public int size() {
        return tuples.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Block[" + tuples.size() + " tuples]:\n");
        for (Tuple t : tuples) {
            sb.append("  ").append(t).append("\n");
        }
        return sb.toString();
    }
}
