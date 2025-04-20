package com.dbproject.join;

import java.util.*;

public class HashJoin {
    private final VirtualDisk disk;
    private final MainMemory memory;
    private final HashFunction hash;
    private final int M;  // memory blocks
    private final int P;  // partitions = M−1

    public HashJoin(VirtualDisk disk, MainMemory memory) {
        this.disk   = disk;
        this.memory = memory;
        this.M      = memory.getBlockCount();
        this.P      = M - 1;
        this.hash   = new HashFunction(P);
    }

    // Two‑pass hash join
    public List<JoinTuple> performTwoPassJoin(List<Integer> rBlocks,
                                              List<Integer> sBlocks) {
        disk.resetIoCount();

        //partition both relations
        List<List<Integer>> rParts = partitionRelation(rBlocks);
        List<List<Integer>> sParts = partitionRelation(sBlocks);

        //for each partition pair, join
        List<JoinTuple> out = new ArrayList<>();
        for (int i = 0; i < P; i++) {
            if (!rParts.get(i).isEmpty() && !sParts.get(i).isEmpty()) {
                out.addAll(joinPartitionPair(rParts.get(i), sParts.get(i)));
            }
        }
        return out;
    }

    // One‑pass hash join when one relation fits in memory 
    public List<JoinTuple> performOnePassJoin(List<Integer> rBlocks,
                                              List<Integer> sBlocks) {
        disk.resetIoCount();

        // choose smaller relation to build hash
        boolean buildR = rBlocks.size() <= sBlocks.size();
        List<Integer> build = buildR ? rBlocks : sBlocks;
        List<Integer> probe = buildR ? sBlocks : rBlocks;

        // build in‑memory hash table
        Map<Integer,List<Tuple>> ht = new HashMap<>();
        memory.clearBlocks();
        for (int bIdx : build) {
            DiskBlock db = disk.readBlock(bIdx);
            memory.loadBlock(0, db);
            for (Tuple t : memory.getBlock(0).getTuples()) {
                ht.computeIfAbsent(t.getKeyB(), k -> new ArrayList<>()).add(t);
            }
        }

        // probe
        List<JoinTuple> out = new ArrayList<>();
        for (int bIdx : probe) {
            DiskBlock db = disk.readBlock(bIdx);
            memory.loadBlock(0, db);
            for (Tuple pt : memory.getBlock(0).getTuples()) {
                List<Tuple> matches = ht.get(pt.getKeyB());
                if (matches != null) {
                    for (Tuple bt : matches) {
                        if (buildR) {
                            out.add(new JoinTuple(bt.getOther(), bt.getKeyB(), pt.getOther()));
                        } else {
                            out.add(new JoinTuple(pt.getOther(), pt.getKeyB(), bt.getOther()));
                        }
                    }
                }
            }
        }
        return out;
    }

    // partitioning
    private List<List<Integer>> partitionRelation(List<Integer> blocks) {
        // buffers for each partition
        List<List<Tuple>> buffers = new ArrayList<>(P);
        for (int i = 0; i < P; i++) buffers.add(new ArrayList<>());

        // scan blocks, hash tuples into buffers
        for (int bIdx : blocks) {
            DiskBlock db = disk.readBlock(bIdx);
            memory.loadBlock(0, db);
            for (Tuple t : memory.getBlock(0).getTuples()) {
                int p = hash.hash1(t.getKeyB());
                buffers.get(p).add(t);
            }
        }

        // write each buffer out in disk blocks
        List<List<Integer>> parts = new ArrayList<>(P);
        for (int i = 0; i < P; i++) {
            List<Tuple> buf = buffers.get(i);
            List<Integer> part = new ArrayList<>();
            for (int j = 0; j < buf.size(); j += DiskBlock.getMaxTuples()) {
                List<Tuple> slice = buf.subList(j, Math.min(j + DiskBlock.getMaxTuples(), buf.size()));
                int newIdx = disk.writeBlock(-1, new DiskBlock(slice));
                part.add(newIdx);
            }
            parts.add(part);
        }
        return parts;
    }

    // Phase 2 in‐memory join of partition i 
    private List<JoinTuple> joinPartitionPair(List<Integer> rPart,
                                              List<Integer> sPart) {
        // build R's hash table
        Map<Integer,List<Tuple>> ht = new HashMap<>();
        memory.clearBlocks();
        for (int bIdx : rPart) {
            DiskBlock db = disk.readBlock(bIdx);
            memory.loadBlock(0, db);
            for (Tuple t : memory.getBlock(0).getTuples()) {
                ht.computeIfAbsent(t.getKeyB(), k -> new ArrayList<>()).add(t);
            }
        }

        // probe S's blocks
        List<JoinTuple> out = new ArrayList<>();
        for (int bIdx : sPart) {
            DiskBlock db = disk.readBlock(bIdx);
            memory.loadBlock(0, db);
            for (Tuple sT : memory.getBlock(0).getTuples()) {
                List<Tuple> matches = ht.get(sT.getKeyB());
                if (matches != null) {
                    for (Tuple rT : matches) {
                        out.add(new JoinTuple(rT.getOther(), rT.getKeyB(), sT.getOther()));
                    }
                }
            }
        }
        return out;
    }

    public int getDiskIoCount() {
        return disk.getIoCount();
    }
}
