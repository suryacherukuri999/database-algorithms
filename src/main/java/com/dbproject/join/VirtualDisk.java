package com.dbproject.join;

import java.util.ArrayList;
import java.util.List;

public class VirtualDisk {
    private final List<DiskBlock> blocks = new ArrayList<>();
    private int ioCount = 0;

    public int size() {
        return blocks.size();
    }

    public int getIoCount() {
        return ioCount;
    }

    public void resetIoCount() {
        ioCount = 0;
    }

    public DiskBlock readBlock(int idx) {
        if (idx < 0 || idx >= blocks.size()) {
            throw new IndexOutOfBoundsException("Invalid disk block index: " + idx);
        }
        ioCount++;
        return blocks.get(idx);
    }

    public int writeBlock(int idx, DiskBlock block) {
        ioCount++;
        if (idx == -1) {
            blocks.add(block);
            return blocks.size() - 1;
        }
        if (idx < 0 || idx >= blocks.size()) {
            throw new IndexOutOfBoundsException("Invalid disk block index: " + idx);
        }
        blocks.set(idx, block);
        return idx;
    }

    public List<Integer> storeRelation(List<Tuple> tuples) {
        List<Integer> out = new ArrayList<>();
        List<Tuple> buffer = new ArrayList<>();
        for (Tuple t : tuples) {
            buffer.add(t);
            if (buffer.size() == DiskBlock.getMaxTuples()) {
                out.add(writeBlock(-1, new DiskBlock(buffer)));
                buffer = new ArrayList<>();
            }
        }
        if (!buffer.isEmpty()) {
            out.add(writeBlock(-1, new DiskBlock(buffer)));
        }
        return out;
    }

    public void printInfo() {
        System.out.println("VirtualDisk: blocks=" + blocks.size() + "  I/O=" + ioCount);
    }
}
