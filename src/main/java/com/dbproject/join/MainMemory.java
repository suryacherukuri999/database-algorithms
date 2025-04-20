// src/main/java/com/dbproject/join/MainMemory.java
package com.dbproject.join;

import java.util.ArrayList;
import java.util.List;

public class MainMemory {
    public static final int BLOCK_COUNT = 15;
    private final DiskBlock[] blocks = new DiskBlock[BLOCK_COUNT];
    private int used = 0;

    public MainMemory() {
        clearBlocks();
    }

    public int getBlockCount() {
        return BLOCK_COUNT;
    }

    public void loadBlock(int memIdx, DiskBlock db) {
        if (memIdx < 0 || memIdx >= BLOCK_COUNT) {
            throw new IndexOutOfBoundsException("Invalid memory index: " + memIdx);
        }
        blocks[memIdx] = db;
        used = Math.max(used, memIdx + 1);
    }

    public DiskBlock getBlock(int memIdx) {
        if (memIdx < 0 || memIdx >= used) {
            throw new IndexOutOfBoundsException("Invalid memory index: " + memIdx);
        }
        return blocks[memIdx];
    }

    public void clearBlocks() {
        for (int i = 0; i < BLOCK_COUNT; i++) {
            blocks[i] = new DiskBlock();
        }
        used = 0;
    }

    public void printContents() {
        System.out.println("MainMemory: used=" + used + "/" + BLOCK_COUNT);
        for (int i = 0; i < used; i++) {
            System.out.println(" MemBlock " + i + ": " + blocks[i]);
        }
    }
}
